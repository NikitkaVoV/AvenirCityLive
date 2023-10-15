package ru.nikitavov.avenir.web.message.realization.crud.request.read;

import lombok.Data;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.realization.crud.base.request.IReadEntityRequest;

@Data
public class ReadEntityRequest<ID, E extends IEntity<ID>> implements IReadEntityRequest<ID, E> {
}
