package ru.nikitavov.avenir.web.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.service.auth.AuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@Log4j2
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationUser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = authenticationUser.getJwtFromRequest(request);
            UserPrincipal userPrincipal = authenticationUser.authenticationUserByJwt(jwt);
            if (userPrincipal != null) {
                authenticationUser.authenticationUser(userPrincipal);
            }

        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
