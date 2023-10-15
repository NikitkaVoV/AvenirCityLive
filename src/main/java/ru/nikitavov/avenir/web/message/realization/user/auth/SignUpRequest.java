package ru.nikitavov.avenir.web.message.realization.user.auth;

public record SignUpRequest(String email, String password, String redirectUrl) {
}
