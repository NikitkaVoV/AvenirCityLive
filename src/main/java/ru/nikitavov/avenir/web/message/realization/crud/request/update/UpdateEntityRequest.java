package ru.nikitavov.avenir.web.message.realization.crud.request.update;

import lombok.Data;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IUpdateEntityRequest;

import java.util.Map;

@Data
public class UpdateEntityRequest<ID, E extends IEntity<ID>> implements IUpdateEntityRequest<ID, E> {
    private Map<String, String> changedFields;
}
