package ru.nikitavov.avenir.web.security.exception;

public class OAuth2AuthenticationProcessingException extends AuthenticationWithCodeException {

    public OAuth2AuthenticationProcessingException(String msg, int code) {
        super(msg, code);
    }
}
