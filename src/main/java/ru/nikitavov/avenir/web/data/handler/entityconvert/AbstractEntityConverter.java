package ru.nikitavov.avenir.web.data.handler.entityconvert;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;
import ru.nikitavov.avenir.web.message.model.responsemessage.ResponseMessageFactory;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.data.action.ActionResultType;
import ru.nikitavov.avenir.web.data.action.realization.ActionResult;

public abstract class AbstractEntityConverter<ID, E extends IEntity<ID>,
        RT_CREATE extends IRequest & ICreateEntity<E>,
        RT_UPDATE extends IRequest & IUpdateEntity<E>
        > implements IEntityConverter<ID, E, RT_CREATE, RT_UPDATE> {

    @Override
    public ActionResult<E> requestCreateToEntity(RT_CREATE request) {
        try {
            return new ActionResult<>(createEntity(request));
        } catch (Exception e) {
            e.printStackTrace();
            return new ActionResult<>(null, ActionResultType.SERVER_ERROR, ResponseMessageFactory.createMessageThrow(e));
        }
    }

    @Override
    public ActionResult<E> requestUpdateToEntity(ID id, RT_UPDATE request) {
        try {
            return new ActionResult<>(updateEntity(id, request));
        } catch (Exception e) {
            e.printStackTrace();
            return new ActionResult<>(null, ActionResultType.SERVER_ERROR, ResponseMessageFactory.createMessageThrow(e));
        }
    }

    protected abstract E createEntity(RT_CREATE request);

    protected abstract E updateEntity(ID id, RT_UPDATE request);
}
