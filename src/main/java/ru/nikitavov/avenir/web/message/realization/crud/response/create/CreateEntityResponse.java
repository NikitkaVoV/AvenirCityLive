package ru.nikitavov.avenir.web.message.realization.crud.response.create;

import lombok.Data;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.response.ICreateEntityResponse;

@Data
public class CreateEntityResponse<ID, E extends IEntity<ID>> implements ICreateEntityResponse<ID, E> {
    private final ID id;
}
