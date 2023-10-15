package ru.nikitavov.avenir.web.message.realization.user.auth;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record LoginRequest(String email, String password) implements IRequest {

}
