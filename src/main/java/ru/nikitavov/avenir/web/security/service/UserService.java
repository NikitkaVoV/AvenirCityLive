package ru.nikitavov.avenir.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.mail.MailChange;
import ru.nikitavov.avenir.database.model.security.mail.TempUser;
import ru.nikitavov.avenir.database.repository.realisation.*;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.avenir.general.util.DateUtil;
import ru.nikitavov.avenir.web.security.AppProperties;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.permission.Roles;
import ru.nikitavov.avenir.web.security.util.KeyGenerator;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;
    private final RoleRepository roleRepository;
    private final AppProperties properties;
    private final TempUserRepository tempUserRepository;
    private final MailChangeRepository mailChangeRepository;

    public Optional<User> getUser(UserPrincipal principal) {
        return userRepository.findById(principal.getId());
    }

    public TempUser startRegistration(String email, String password, String redirectUrl) {
        Date expiryDate = DateUtil.createExpiryDate(properties.getMisc().getTokens().getRegistrationTokenExpirationMsec());

        String token = KeyGenerator.string(128);

        TempUser tempUser = TempUser.builder().redirectUrl(redirectUrl).password(password)
                .email(email).expiryDate(expiryDate).token(token).build();

        return tempUserRepository.save(tempUser);
    }

    public User registryUser(String email, String password) {
        User user = createEmptyUser();
        user.setEmail(email);
        user.setPassword(password);

        return saveNewUser(user);
    }

    public User registryUser(SocialNetworkType type, String id) {
        User user = createEmptyUser();
        user = saveNewUser(user);

        addSocialNetwork(type, id, user);

        return user;
    }

    private User saveNewUser(User user) {
        User finalUser = user;
        roleRepository.findByName(Roles.USER.roleName).map(role -> finalUser.getRoles().add(role));
        roleRepository.findByName(Roles.UNVERIFIED.roleName).map(role -> finalUser.getRoles().add(role));

        user = userRepository.save(finalUser);

        return user;
    }

    public void addSocialNetwork(SocialNetworkType type, String id, User user) {
        LinkedSocialNetwork socialNetwork = LinkedSocialNetwork.builder().socialNetworkType(type).user(user)
                .userSocialNetworkId(id).build();
        linkedSocialNetworkRepository.save(socialNetwork);
    }

    public User createEmptyUser() {
        User user = new User();
        user.setUuid(UUID.randomUUID());

        return user;
    }

    public User getActiveUser() {
        return getActiveUserOptional().orElse(null);
    }

    public Optional<User> getActiveUserOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserPrincipal principal) {
            return userRepository.findById(principal.getId());
        }

        return Optional.empty();
    }

    public UserPrincipal getActiveUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserPrincipal principal) {
            return principal;
        }

        return null;
    }

    public MailChange changeMail(String email, String redirectUrl) {
        Date expiryDate = DateUtil.createExpiryDate(properties.getMisc().getTokens().getChangeEmailTokenExpirationMsec());

        String token = KeyGenerator.string(128);

        MailChange mailChange = MailChange.builder().redirectUrl(redirectUrl).user(getActiveUser())
                .email(email).expiryDate(expiryDate).token(token).build();

        return mailChangeRepository.save(mailChange);
    }
}
