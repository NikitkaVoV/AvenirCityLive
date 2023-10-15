package ru.nikitavov.avenir.web.security.data;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    private final Integer id;
    private final String email;
    private final String password;
    private final UUID uuid;
    private final Collection<String> permissions;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Integer id, String email, String password, UUID uuid, Collection<String> permissions) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.uuid = uuid;
        this.permissions = permissions;
        this.authorities = createAuthorities(permissions);
    }

    protected Collection<? extends GrantedAuthority> createAuthorities(Collection<String> permissions) {
       return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return uuid.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(uuid);
    }
}
