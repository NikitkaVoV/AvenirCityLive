package ru.nikitavov.avenir.web.message.realization.crud.base.response;

import org.springframework.http.HttpStatus;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IReadEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public interface IReadEntityResponse<E extends IEntity<?>> extends IReadEntity<E>, IResponse {
    @Override
    default HttpStatus defaultStatus() {
        return HttpStatus.OK;
    }
}
