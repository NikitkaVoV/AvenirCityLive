package ru.nikitavov.avenir.web.data.handler.crud.manipulation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.data.action.realization.ActionResult;
import ru.nikitavov.avenir.database.repository.RepositoryHelper;

@RequiredArgsConstructor
public abstract class AbstractCrudService<R extends JpaRepository<E, ID>, E extends IEntity<ID>, ID> implements ICrudService<E, ID> {
    protected final R repository;
    protected final RepositoryHelper helper;

    @Override
    public ActionResult<E> create(E entity) {
        return helper.save(repository, entity);
    }

    @Override
    public ActionResult<E> read(ID id) {
        return helper.findById(repository, id);
    }

    @Override
    public ActionResult<E> update(ID id, E entity) {
        return helper.update(repository, id, entity);
    }

    @Override
    public ActionResult<E> delete(ID id) {
        return helper.delete(repository, id);
    }

    public boolean existId(ID id) {
        if (id == null) return false;
        return  repository.existsById(id);
    }
}
