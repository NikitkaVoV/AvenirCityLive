package ru.nikitavov.avenir.web.controller.rest.user;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.repository.realisation.RoleRepository;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.user.role.UserAddRoleRequest;
import ru.nikitavov.avenir.web.message.realization.user.role.UserAvailableRolesResponse;
import ru.nikitavov.avenir.web.message.realization.user.role.UserRemoveRoleRequest;
import ru.nikitavov.avenir.web.security.data.PermissionAuthorize;
import ru.nikitavov.avenir.web.security.permission.Permissions;
import ru.nikitavov.avenir.web.security.permission.Roles;
import ru.nikitavov.avenir.web.security.service.UserRoleService;
import ru.nikitavov.avenir.web.security.service.UserService;
import ru.nikitavov.avenir.web.util.role.RoleUtil;

import java.util.Optional;

@RestController
@RequestMapping("/user/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PermissionAuthorize(Permissions.ROLE_MANAGER_AVAILABLE)
    @GetMapping("/available")
    public ResponseEntity<MessageWrapper<UserAvailableRolesResponse>> availableRoles() {
        UserAvailableRolesResponse response = new UserAvailableRolesResponse(userRoleService.getAvailableRoles());
        return ResponseEntity.ok(MessageWrapper.create(response));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - роль не найдена;" +
                    "<br>2 - пользователь с таким id не найден;" +
                    "<br>3 - у пользователя есть эта роль"),
    })
    @PostMapping("/add")
    @PermissionAuthorize(Permissions.ROLE_MANAGER_ADD)
    public ResponseEntity<MessageWrapper<Void>> addRole(@RequestBody UserAddRoleRequest request) {
        Roles role = Roles.findByName(request.roleName());
        if (role == null)
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        Optional<User> optionalUser = userRepository.findById(request.userId());
        if (optionalUser.isEmpty())
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));

        User user = optionalUser.get();
        if (RoleUtil.containRole(user.getRoles(), role))
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));

        user.getRoles().add(roleRepository.findByName(role.roleName).get());
        userRepository.save(user);
        return ResponseEntity.ok(MessageWrapper.create(null, 0));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - роль не найдена;" +
                    "<br>2 - пользователь с таким id не найден;" +
                    "<br>3 - у пользователя нет этой роли"),
    })
    @PermissionAuthorize(Permissions.ROLE_MANAGER_REMOVE)
    @PostMapping("/remove")
    public ResponseEntity<MessageWrapper<Void>> removeRole(@RequestBody UserRemoveRoleRequest request) {
        Roles role = Roles.findByName(request.roleName());
        if (role == null)
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        Optional<User> optionalUser = userRepository.findById(request.userId());
        if (optionalUser.isEmpty())
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));

        User user = optionalUser.get();
        if (!RoleUtil.containRole(user.getRoles(), role))
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));

        user.getRoles().remove(roleRepository.findByName(role.roleName).get());
        userRepository.save(user);
        return ResponseEntity.ok(MessageWrapper.create(null, 0));
    }

}
