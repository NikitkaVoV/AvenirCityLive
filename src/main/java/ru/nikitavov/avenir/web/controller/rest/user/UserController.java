package ru.nikitavov.avenir.web.controller.rest.user;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.user.misc.FindUsersRequest;
import ru.nikitavov.avenir.web.message.realization.user.misc.FindUsersResponse;
import ru.nikitavov.avenir.web.message.realization.user.misc.UserPermissionsResponse;
import ru.nikitavov.avenir.web.security.data.PermissionAuthorize;
import ru.nikitavov.avenir.web.security.permission.Permissions;
import ru.nikitavov.avenir.web.security.service.UserPrincipalService;
import ru.nikitavov.avenir.web.security.service.UserRoleService;
import ru.nikitavov.avenir.web.security.service.UserService;
import ru.nikitavov.avenir.web.util.role.PermissionService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserPrincipalService principalService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final PermissionService permissionService;

    @PermissionAuthorize(Permissions.PERMISSIONS_ME)
    @GetMapping("/permissions/me")
    public ResponseEntity<MessageWrapper<UserPermissionsResponse>> mePermissions() {
        User user = userService.getActiveUser();
        Set<String> permissions = principalService.geUserPermissions(user);

        return ResponseEntity.ok(MessageWrapper.create(new UserPermissionsResponse(permissions)));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит информацию о пользователях и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - page или size имею не допустимое значение"
            ),
    })
    @GetMapping("/find/all")
    @PermissionAuthorize(Permissions.VERIFIED_FIND)
    public ResponseEntity<MessageWrapper<FindUsersResponse>> findUsers(FindUsersRequest request) {

        if (request.page() < 0 || request.size() <= 0) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }

        String[] split = request.filterString().split(" ");

        String name = "";
        String surname = "";
        String patronymic = "";

        if (split.length >= 1) surname = split[0];
        if (split.length >= 2) name = split[1];
        if (split.length >= 3) patronymic = split[2];

        Page<User> users = userRepository.findUsersByUuidOrFullname(request.filterString(), name, surname, patronymic,
                PageRequest.of(request.page(), request.size(), Sort.by("surname", "name", "patronymic")));

        List<FindUsersResponse.UserInfo> userInfos = users.stream().map(user -> new FindUsersResponse.UserInfo(user, getUserRoles(user))).toList();
        FindUsersResponse response = new FindUsersResponse(userInfos, users.getTotalElements(), users.getTotalPages());

        return ResponseEntity.ok(MessageWrapper.create(response));
    }

    private List<String> getUserRoles(User user) {
        if (!permissionService.containPermissionActiveUser(Permissions.ROLE_MANAGER_VIEW)) {
            return Collections.emptyList();
        }

        return userRoleService.userRolesFromAvailable(user);
    }
}
