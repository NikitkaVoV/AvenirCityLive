package ru.nikitavov.avenir.web.data.handler.crud.message;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.data.handler.crud.message.interfaces.CreateResponseAction;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;
import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;
import ru.nikitavov.avenir.web.message.model.responsemessage.FieldResponseMessage;
import ru.nikitavov.avenir.web.message.model.responsemessage.ResponseMessageFactory;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IDeleteEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IReadEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.ICreateEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IDeleteEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IReadEntityRequest;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IUpdateEntityRequest;
import ru.nikitavov.avenir.web.data.action.ActionResultType;
import ru.nikitavov.avenir.web.data.action.realization.ActionResult;
import ru.nikitavov.avenir.web.data.action.realization.RequestActionResult;
import ru.nikitavov.avenir.web.data.handler.entityconvert.IEntityConverter;
import ru.nikitavov.avenir.web.data.handler.crud.manipulation.ICrudService;
import ru.nikitavov.avenir.general.util.CollectionUtil;

@RequiredArgsConstructor
public abstract class AbstractCrudMessageService<
        ID, CS extends ICrudService<E, ID>, C extends IEntityConverter<ID, E, RT_CREATE, RT_UPDATE>, E extends IEntity<ID>,
        RT_CREATE extends ICreateEntityRequest<E>, RE_CREATE extends IResponse & ICreateEntity<E>,
        RT_READ extends IReadEntityRequest<ID, E>, RE_READ extends IResponse & IReadEntity<E>,
        RT_UPDATE extends IUpdateEntityRequest<ID, E>, RE_UPDATE extends IResponse & IUpdateEntity<E>,
        RT_DELETE extends IDeleteEntityRequest<ID, E>, RE_DELETE extends IResponse & IDeleteEntity<E>
        > implements ICrudMessageService<E, RT_CREATE, RE_CREATE, RT_READ, RE_READ, RT_UPDATE, RE_UPDATE, RT_DELETE, RE_DELETE> {
    protected final CS service;
    protected final C converter;
    protected CreateResponseAction<E, RE_CREATE> createResponseCreate;
    protected CreateResponseAction<E, RE_READ> createResponseRead;
    protected CreateResponseAction<E, RE_UPDATE> createResponseUpdate;
    protected CreateResponseAction<E, RE_DELETE> createResponseDelete;

    @Autowired
    protected ConversionService conversionService;

    @Override
    public RequestActionResult<RE_CREATE, E> create(RT_CREATE request) {
        ActionResult<E> resultConvert = converter.requestCreateToEntity(request);
        if (!resultConvert.isOk()) return new RequestActionResult<>(null, resultConvert);

        ActionResult<E> result = service.create(resultConvert.getData());
        RE_CREATE response = null;
        if (result.isOk()) {
            ActionResult<RE_CREATE> createResult = createResponse(result.getData(), createResponseCreate);
            response = createResult.getData();
            if (createResult.isServerError()) {
                result.setActionType(createResult.getActionType());
            }
            CollectionUtil.insertListToList(result.getMessages(), createResult.getMessages());
        }
        return new RequestActionResult<>(response, result);
    }

    @Override
    public RequestActionResult<RE_READ, E> read(String id, RT_READ request) {
        ActionResult<ID> idActionResult = convertStringToId(id);
        if (!idActionResult.isOk()) return new RequestActionResult<>(null, new ActionResult<>(null, idActionResult));

        ActionResult<E> result = service.read(idActionResult.getData());
        RE_READ response = null;
        if (result.isOk()) {
            ActionResult<RE_READ> readResult = createResponse(result.getData(), createResponseRead);
            response = readResult.getData();
            if (readResult.isServerError()) {
                result.setActionType(readResult.getActionType());
            }
            CollectionUtil.insertListToList(result.getMessages(), readResult.getMessages());
        }
        return new RequestActionResult<>(response, result);
    }

    @Override
    public RequestActionResult<RE_UPDATE, E> update(String id, RT_UPDATE request) {
        ActionResult<ID> idActionResult = convertStringToId(id);
        if (!idActionResult.isOk()) return new RequestActionResult<>(null, new ActionResult<>(null, idActionResult));

        if (!service.existId(idActionResult.getData())) {
            return new RequestActionResult<>(null, new ActionResult<>(null, ActionResultType.NOT_FOUND));
        }

        ActionResult<E> resultConvert = converter.requestUpdateToEntity(idActionResult.getData(), request);
        if (!resultConvert.isOk()) return new RequestActionResult<>(null, resultConvert);

        ActionResult<E> result = service.update(idActionResult.getData(), resultConvert.getData());
        RE_UPDATE response = null;
        if (result.isOk()) {
            ActionResult<RE_UPDATE> createResult = createResponse(result.getData(), createResponseUpdate);
            response = createResult.getData();
            if (createResult.isServerError()) {
                result.setActionType(createResult.getActionType());
            }
            CollectionUtil.insertListToList(result.getMessages(), createResult.getMessages());
        }
        return new RequestActionResult<>(response, result);
    }

    @Override
    public RequestActionResult<RE_DELETE, E> delete(String id, RT_DELETE request) {
        ActionResult<ID> idActionResult = convertStringToId(id);
        if (!idActionResult.isOk()) return new RequestActionResult<>(null, new ActionResult<>(null, idActionResult));

        ActionResult<E> result = service.delete(idActionResult.getData());
        RE_DELETE response = null;
        if (result.isOk()) {
            ActionResult<RE_DELETE> deleteResult = createResponse(result.getData(), createResponseDelete);
            response = deleteResult.getData();
            if (deleteResult.isServerError()) {
                result.setActionType(deleteResult.getActionType());
            }
            CollectionUtil.insertListToList(result.getMessages(), deleteResult.getMessages());
        }
        return new RequestActionResult<>(response, result);
    }

    protected ActionResult<ID> convertStringToId(String id) {
        ResolvableType resolvableType = ResolvableType.forClass(getClass()).as(AbstractCrudMessageService.class);
        ResolvableType idType = resolvableType.getGeneric(0);
        Class<?> idClass = idType.toClass();
        if (!conversionService.canConvert(id.getClass(), idClass)) {
            return new ActionResult<>(null, ActionResultType.SERVER_ERROR,
                    new FieldResponseMessage("id", "id", "Don`t convert String to " + idClass.getSimpleName()));
        }

        try {
            ID convertedId = (ID) conversionService.convert(id, idClass);
            return new ActionResult<>(convertedId);
        } catch (Exception e) {
            return new ActionResult<>(null, ActionResultType.INVALID_PARAMS,
                    ResponseMessageFactory.createMessageLocalized("message.warn.convert.id", idClass.getSimpleName()));
        }
    }

    protected <RE extends IResponse> ActionResult<RE> createResponse(E entity, CreateResponseAction<E, RE> createResponseAction) {
        RE response = null;
        ActionResultType type = ActionResultType.OK;
        IResponseMessage[] messages = new IResponseMessage[1];
        try {
            response = createResponseAction.create(entity);
        } catch (Exception e) {
            e.printStackTrace();
            type = ActionResultType.SERVER_ERROR;
            messages[0] = ResponseMessageFactory.createMessageThrow(e);
        }
        return new ActionResult<>(response, type, messages);
    }
}
