package ru.nikitavov.avenir.web.message.realization.crud.base.request;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public interface IUpdateEntityRequest<ID, E extends IEntity<ID>> extends IUpdateEntity<E>, IRequest {
}
