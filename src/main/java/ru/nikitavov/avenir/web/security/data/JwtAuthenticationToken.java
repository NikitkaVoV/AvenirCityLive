package ru.nikitavov.avenir.web.security.data;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserPrincipal userPrincipal;

    public JwtAuthenticationToken(UserPrincipal userPrincipal) {
        super(userPrincipal.getAuthorities());
        this.userPrincipal = userPrincipal;
    }

    @Override
    public Object getCredentials() {
        return userPrincipal;
    }

    @Override
    public Object getPrincipal() {
        return userPrincipal;
    }
}
