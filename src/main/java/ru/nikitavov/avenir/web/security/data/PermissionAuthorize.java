package ru.nikitavov.avenir.web.security.data;

import ru.nikitavov.avenir.web.security.permission.Permissions;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionAuthorize {
    Permissions value();
}