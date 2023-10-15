package ru.nikitavov.avenir.web.message.realization.crud.request.delete;

import lombok.Data;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IDeleteEntityRequest;

@Data
public class DeleteEntityRequest<ID, E extends IEntity<ID>> implements IDeleteEntityRequest<ID, E> {
}
