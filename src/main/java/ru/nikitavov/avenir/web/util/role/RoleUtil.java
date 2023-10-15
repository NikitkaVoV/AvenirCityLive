package ru.nikitavov.avenir.web.util.role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.nikitavov.avenir.database.model.base.Role;
import ru.nikitavov.avenir.web.security.permission.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoleUtil {

    public static List<GrantedAuthority> rolesToGrantedAuthority(Set<Role> roles) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>(roles.size());

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().toUpperCase().replaceAll(" ", "_")));
        }

        return authorities;
    }

    public static boolean containRole(Set<Role> roles, Roles type) {
        for (Role role : roles) {
            if (role.getName().equals(type.roleName)) {
                return true;
            }
        }

        return false;
    }

}
