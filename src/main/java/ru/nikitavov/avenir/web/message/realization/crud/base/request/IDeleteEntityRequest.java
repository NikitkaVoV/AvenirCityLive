package ru.nikitavov.avenir.web.message.realization.crud.base.request;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IDeleteEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public interface IDeleteEntityRequest<ID, E extends IEntity<?>> extends IDeleteEntity<E>, IRequest {
}
