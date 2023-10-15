package ru.nikitavov.avenir.web.data.action.realization;

import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;
import ru.nikitavov.avenir.web.data.action.ActionResultType;

public class RepositoryActionResult<E extends IEntity<?>> extends ActionResult<E> {
    public RepositoryActionResult(E data, ActionResultType type, IResponseMessage... messages) {
        super(data, type, messages);
    }
}
