package ru.nikitavov.avenir.web.message.realization.user.profile.update;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record ProfileUpdateFullNameRequest(String name, String surname, String patronymic) implements IRequest {
}
