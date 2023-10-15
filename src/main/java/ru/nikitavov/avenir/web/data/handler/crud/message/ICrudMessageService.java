package ru.nikitavov.avenir.web.data.handler.crud.message;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IDeleteEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IReadEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;
import ru.nikitavov.avenir.web.data.action.realization.RequestActionResult;

public interface ICrudMessageService<
        E extends IEntity<?>,
        RT_CREATE extends IRequest & ICreateEntity<E>,
        RE_CREATE extends IResponse & ICreateEntity<E>,
        RT_READ extends IRequest & IReadEntity<E>,
        RE_READ extends IResponse & IReadEntity<E>,
        RT_UPDATE extends IRequest & IUpdateEntity<E>,
        RE_UPDATE extends IResponse & IUpdateEntity<E>,
        RT_DELETE extends IRequest & IDeleteEntity<E>,
        RE_DELETE extends IResponse & IDeleteEntity<E>
        > {
    RequestActionResult<RE_CREATE, E> create(RT_CREATE request);

    RequestActionResult<RE_READ, E> read(String id, RT_READ request);

    RequestActionResult<RE_UPDATE, E> update(String id, RT_UPDATE request);

    RequestActionResult<RE_DELETE, E> delete(String id, RT_DELETE request);
}
