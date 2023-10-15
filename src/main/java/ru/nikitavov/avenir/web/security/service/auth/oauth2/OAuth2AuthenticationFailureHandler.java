package ru.nikitavov.avenir.web.security.service.auth.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nikitavov.avenir.web.security.exception.AuthenticationWithCodeException;
import ru.nikitavov.avenir.web.util.CookieUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.nikitavov.avenir.web.security.service.auth.oauth2.HttpCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {

        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(targetUrl);

        uriBuilder.queryParam("error", exception.getLocalizedMessage());

        if (exception instanceof AuthenticationWithCodeException auth2Exception) {
            uriBuilder.queryParam("code", auth2Exception.getCode());
        }

        targetUrl = uriBuilder.build().toUriString();

        httpCookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
