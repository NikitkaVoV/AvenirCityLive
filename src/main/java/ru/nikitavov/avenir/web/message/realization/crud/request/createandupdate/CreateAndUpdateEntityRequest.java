package ru.nikitavov.avenir.web.message.realization.crud.request.createandupdate;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.CrudType;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.ICreateEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IUpdateEntityRequest;

public abstract class CreateAndUpdateEntityRequest<ID, E extends IEntity<ID>> implements ICreateEntityRequest<E> , IUpdateEntityRequest<ID, E> {
    @Override
    public CrudType getType() {
        return CrudType.CREATE_AND_UPDATE;
    }
}
