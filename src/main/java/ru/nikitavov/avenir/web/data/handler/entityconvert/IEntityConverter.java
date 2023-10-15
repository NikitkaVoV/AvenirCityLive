package ru.nikitavov.avenir.web.data.handler.entityconvert;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;
import ru.nikitavov.avenir.web.data.action.realization.ActionResult;

public interface IEntityConverter<ID , E extends IEntity<ID>,
        RT_CREATE extends IRequest & ICreateEntity<E>,
        RT_UPDATE extends IRequest & IUpdateEntity<E>
        > {
    ActionResult<E> requestCreateToEntity(RT_CREATE request);

    ActionResult<E> requestUpdateToEntity(ID id, RT_UPDATE request);
}
