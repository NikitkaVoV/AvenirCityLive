package ru.nikitavov.avenir.web.security.service.auth;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.Constants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.general.model.tuple.Tuple3;
import ru.nikitavov.avenir.web.security.data.JwtAuthenticationToken;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.service.UserDetailsServiceImpl;
import ru.nikitavov.avenir.web.security.util.RequestHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;


    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.AUTHORIZATION_HEADER_NAME);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public UserPrincipal authenticationUserByJwt(String jwtToken) {
        if (StringUtils.hasText(jwtToken) && tokenProvider.validateToken(jwtToken)) {
            String uuid = tokenProvider.getUserUUIDFromToken(jwtToken);

            return userDetailsService.loadUserByUsername(uuid);
        }

        return null;
    }

    public void authenticationUser(UserPrincipal userPrincipal) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(userPrincipal);

        HttpServletRequest request = RequestHelper.currentRequest();
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public String callbackUrlAfterAuthByUrl(User user, String targetUrl) {
        Tuple3<String, String, Date> result = tokenProvider.createTokens(user);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", result.param1())
                .queryParam("refreshToken", result.param2())
                .queryParam("expiry", result.param3().getTime())
                .build().toUriString();
    }
}
