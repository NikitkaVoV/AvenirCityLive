package ru.nikitavov.avenir.web.message.realization.user.profile.update;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record ProfileUpdateMailingResponse(boolean active) implements IRequest {
}
