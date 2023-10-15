package ru.nikitavov.avenir.web.security.exception;

import org.springframework.security.core.AuthenticationException;

public class DisableUserException extends AuthenticationException {
    public DisableUserException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DisableUserException(String msg) {
        super(msg);
    }
}
