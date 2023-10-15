package ru.nikitavov.avenir.web.message.realization.crud.base.request;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IReadEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public interface IReadEntityRequest<ID, E extends IEntity<ID>> extends IReadEntity<E>, IRequest {
}
