package ru.nikitavov.avenir.web.message.realization.crud.base;

import ru.nikitavov.avenir.database.model.IEntity;

public interface IReadEntity<E extends IEntity<?>> extends ICrudEntity<E> {
    @Override
    default CrudType getType() {
        return CrudType.READ;
    }
}
