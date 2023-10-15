package ru.nikitavov.avenir.web.message.realization.user.verified;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

import java.util.UUID;

public record VerifiedAddUserRequest(UUID uuid) implements IRequest {

}
