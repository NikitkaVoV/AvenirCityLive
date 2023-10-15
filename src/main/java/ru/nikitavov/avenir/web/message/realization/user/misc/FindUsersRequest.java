package ru.nikitavov.avenir.web.message.realization.user.misc;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record FindUsersRequest(String filterString, int size, int page) implements IRequest {

}
