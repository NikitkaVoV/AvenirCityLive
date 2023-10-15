package ru.nikitavov.avenir.web.message.realization.crud.base.response;

import org.springframework.http.HttpStatus;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.ICreateEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public interface ICreateEntityResponse<ID, E extends IEntity<ID>> extends ICreateEntity<E>, IResponse {
    ID getId();

    @Override
    default HttpStatus defaultStatus() {
        return HttpStatus.CREATED;
    }
}
