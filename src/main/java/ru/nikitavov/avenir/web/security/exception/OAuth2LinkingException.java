package ru.nikitavov.avenir.web.security.exception;

public class OAuth2LinkingException extends AuthenticationWithCodeException {

    public OAuth2LinkingException(String msg, int code) {
        super(msg, code);
    }
}
