package ru.nikitavov.avenir.web.message.realization.crud.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.nikitavov.avenir.database.model.IEntity;

public interface ICrudEntity<E extends IEntity<?>> {
    @JsonIgnore
    CrudType getType();
}
