package ru.nikitavov.avenir.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.LinkSocialNetworkToken;
import ru.nikitavov.avenir.database.repository.realisation.LinkSocialNetworkTokenRepository;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.service.auth.AuthenticationService;
import ru.nikitavov.avenir.web.security.util.KeyGenerator;
import ru.nikitavov.avenir.web.security.util.TimeUtil;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2SocialNetworkLinkHandler {

    private final LinkSocialNetworkTokenRepository linkSocialNetworkTokenRepository;
    private final AuthenticationService authenticationUser;
    private final UserRepository userRepository;

    @Transactional
    public String createTokenForAdd(String jwt) {
        UserPrincipal userPrincipal = authenticationUser.authenticationUserByJwt(jwt);
        if (userPrincipal == null) return "";

        Optional<User> activeUser = userRepository.findById(userPrincipal.getId());
        if (activeUser.isEmpty()) {
            return "";
        }

        linkSocialNetworkTokenRepository.deleteByUser_Id(activeUser.get().getId());

        String token = KeyGenerator.string(64);
        Date expiryDate = TimeUtil.createExpiryDate(300000); //5 min

        linkSocialNetworkTokenRepository.save(LinkSocialNetworkToken.builder()
                .token(token)
                .user(activeUser.get())
                .expiryDate(expiryDate)
                .build());

        return token;
    }
}
