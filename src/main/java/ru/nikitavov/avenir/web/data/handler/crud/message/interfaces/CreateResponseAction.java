package ru.nikitavov.avenir.web.data.handler.crud.message.interfaces;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

@FunctionalInterface
public interface CreateResponseAction<E extends IEntity<?>, RE extends IResponse> {
    RE create(E entity);
}
