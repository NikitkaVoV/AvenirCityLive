package ru.nikitavov.avenir.web.message.realization.crud.base.response;

import org.springframework.http.HttpStatus;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.IDeleteEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public interface IDeleteEntityResponse<E extends IEntity<?>> extends IDeleteEntity<E>, IResponse {
    @Override
    default HttpStatus defaultStatus() {
        return HttpStatus.NO_CONTENT;
    }
}
