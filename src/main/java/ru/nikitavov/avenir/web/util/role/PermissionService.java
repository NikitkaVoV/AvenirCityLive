package ru.nikitavov.avenir.web.util.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.permission.Permissions;
import ru.nikitavov.avenir.web.security.service.UserService;


@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserService userService;

    public boolean containPermissionActiveUser(Permissions permission) {
        UserPrincipal activeUserPrincipal = userService.getActiveUserPrincipal();
        return activeUserPrincipal.getPermissions().contains(permission.getPermission());
    }
}
