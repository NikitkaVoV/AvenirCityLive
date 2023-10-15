package ru.nikitavov.avenir.web.message.realization.crud.base.response;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IUpdateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public interface IUpdateEntityResponse<E extends IEntity<?>> extends IUpdateEntity<E>, IResponse {

}
