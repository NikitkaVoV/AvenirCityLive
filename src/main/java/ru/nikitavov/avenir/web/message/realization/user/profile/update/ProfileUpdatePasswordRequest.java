package ru.nikitavov.avenir.web.message.realization.user.profile.update;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record ProfileUpdatePasswordRequest(String oldPassword, String newPassword) implements IRequest {
}
