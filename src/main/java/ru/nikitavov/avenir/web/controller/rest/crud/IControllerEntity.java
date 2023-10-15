package ru.nikitavov.schedule.api.controller.rest.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IDeleteEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IReadEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;

public interface IControllerEntity<
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
    ResponseEntity<MessageWrapper<RE_CREATE>> create(RT_CREATE request, BindingResult bindingResult);

    ResponseEntity<MessageWrapper<RE_READ>> read(String id, RT_READ request);

    ResponseEntity<MessageWrapper<RE_UPDATE>> update(String id, RT_UPDATE request);

    ResponseEntity<MessageWrapper<RE_DELETE>> delete(String id, RT_DELETE request);
}
