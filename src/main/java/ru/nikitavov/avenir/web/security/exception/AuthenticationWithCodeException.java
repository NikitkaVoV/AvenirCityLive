package ru.nikitavov.avenir.web.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter

public class AuthenticationWithCodeException extends AuthenticationException {

    private final int code;

    public AuthenticationWithCodeException(String msg, int code) {
        super(msg);

        this.code = code;
    }
}
