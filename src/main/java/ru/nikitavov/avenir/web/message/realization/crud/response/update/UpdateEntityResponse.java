package ru.nikitavov.avenir.web.message.realization.crud.response.update;

import org.springframework.http.HttpStatus;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.response.IUpdateEntityResponse;

public class UpdateEntityResponse<E extends IEntity<?>> implements IUpdateEntityResponse<E> {
    @Override
    public HttpStatus defaultStatus() {
        return HttpStatus.OK;
    }
}
