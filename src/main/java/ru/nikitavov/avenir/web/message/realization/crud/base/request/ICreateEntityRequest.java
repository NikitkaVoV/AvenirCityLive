package ru.nikitavov.avenir.web.message.realization.crud.base.request;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public interface ICreateEntityRequest<E extends IEntity<?>> extends ICreateEntity<E>, IRequest {
}
