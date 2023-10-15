package ru.nikitavov.avenir.web.security.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.Permission;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPrincipalService {
    private final UserRepository userRepository;

    public UserPrincipalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public UserPrincipal create(User user) {
        user = userRepository.findById(user.getId()).get();

        Set<String> permissions = geUserPermissions(user);


        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUuid(),
                permissions
        );
    }

    public Set<String> geUserPermissions(User user) {
        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream().map(Permission::getName))
                .collect(Collectors.toSet());

        permissions.addAll(user.getPermissions().stream().map(Permission::getName).toList());
        return permissions;
    }
}
