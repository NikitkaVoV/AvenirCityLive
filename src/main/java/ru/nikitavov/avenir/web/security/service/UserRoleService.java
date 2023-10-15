package ru.nikitavov.avenir.web.security.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.database.model.base.Role;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.web.security.permission.Roles;

import java.util.Arrays;
import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final List<String> availableRoles = Arrays.asList(Roles.ADMIN.roleName, Roles.TRUSTED.roleName, Roles.SCHEDULE_MANAGER.roleName);

    public List<String> userRolesFromAvailable(User user) {
        return user.getRoles().stream().map(Role::getName).filter(availableRoles::contains).toList();
    }
}
