package ru.nikitavov.avenir.web.controller.rest.crud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.data.action.realization.RequestActionResult;
import ru.nikitavov.avenir.web.data.handler.crud.message.ICrudMessageService;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.ICreateEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IDeleteEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IReadEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IUpdateEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.response.ICreateEntityResponse;
import ru.nikitavov.avenir.web.message.realization.crud.base.response.IDeleteEntityResponse;
import ru.nikitavov.avenir.web.message.realization.crud.base.response.IReadEntityResponse;
import ru.nikitavov.avenir.web.message.realization.crud.base.response.IUpdateEntityResponse;
import ru.nikitavov.avenir.web.security.data.PermissionAuthorize;
import ru.nikitavov.avenir.web.security.permission.Permissions;

@RequiredArgsConstructor
@CrossOrigin
@PermissionAuthorize(Permissions.NONE)
public abstract class AbstractCrudController<
        S extends ICrudMessageService<E, RT_CREATE, RE_CREATE, RT_READ, RE_READ, RT_UPDATE, RE_UPDATE, RT_DELETE, RE_DELETE>,
        E extends IEntity<?>,
        RT_CREATE extends ICreateEntityRequest<E>, RE_CREATE extends ICreateEntityResponse<?, E>,
        RT_READ extends IReadEntityRequest<?, E>, RE_READ extends IReadEntityResponse<E>,
        RT_UPDATE extends IUpdateEntityRequest<?, E>, RE_UPDATE extends IUpdateEntityResponse<E>,
        RT_DELETE extends IDeleteEntityRequest<?, E>, RE_DELETE extends IDeleteEntityResponse<E>
        > implements ru.nikitavov.schedule.api.controller.rest.crud.IControllerEntity<E, RT_CREATE, RE_CREATE, RT_READ, RE_READ, RT_UPDATE, RE_UPDATE, RT_DELETE, RE_DELETE> {

    protected final S service;

    @PostMapping
    @Override
    public ResponseEntity<MessageWrapper<RE_CREATE>> create(RT_CREATE request, BindingResult bindingResult) {
        RequestActionResult<RE_CREATE, E> result = service.create(request);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(result.getActualStatus());
        return builder.body(new MessageWrapper<>(result.getResponse(), result.getMessages()));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<MessageWrapper<RE_READ>> read(@PathVariable String id, RT_READ request) {
        //TODO: Add all
        RequestActionResult<RE_READ, E> result = service.read(id, request);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(result.getActualStatus());
        return builder.body(new MessageWrapper<>(result.getResponse(), result.getMessages()));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<MessageWrapper<RE_UPDATE>> update(@PathVariable String id, RT_UPDATE request) {
        RequestActionResult<RE_UPDATE, E> result = service.update(id, request);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(result.getActualStatus());
        return builder.body(new MessageWrapper<>(result.getResponse(), result.getMessages()));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<MessageWrapper<RE_DELETE>> delete(@PathVariable String id, RT_DELETE request) {
        RequestActionResult<RE_DELETE, E> result = service.delete(id, request);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(result.getActualStatus());
        return builder.body(new MessageWrapper<>(result.getResponse(), result.getMessages()));
    }
}
