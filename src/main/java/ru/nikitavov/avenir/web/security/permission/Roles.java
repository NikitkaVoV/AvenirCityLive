package ru.nikitavov.avenir.web.security.permission;

import org.springframework.util.StringUtils;

public enum Roles {
    USER, ADMIN, SCHEDULE_MANAGER, UNVERIFIED, VERIFIED, TRUSTED;

    public final String roleName;

    Roles() {
        this.roleName = this.name().toLowerCase();
    }

    public static Roles findByName(String name) {
        if (StringUtils.hasText(name)) {
            for (Roles role : values()) {
                if (role.roleName.equals(name)) {
                    return role;
                }
            }
        }
        return null;
    }

}
