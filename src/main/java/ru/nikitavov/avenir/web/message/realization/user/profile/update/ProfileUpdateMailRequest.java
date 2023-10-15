package ru.nikitavov.avenir.web.message.realization.user.profile.update;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record ProfileUpdateMailRequest(String mail, String redirectUrl) implements IRequest {
}
