package ru.nikitavov.avenir.database.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;
import ru.nikitavov.avenir.web.message.model.responsemessage.ResponseMessageFactory;
import ru.nikitavov.avenir.web.data.action.ActionResultType;
import ru.nikitavov.avenir.web.data.action.realization.RepositoryActionResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class RepositoryHelper {
    private final Validator validator;

    public RepositoryHelper() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public <ID, E extends IEntity<ID>> RepositoryActionResult<E> save(JpaRepository<E, ID> repository, E entity) {
        Set<ConstraintViolation<E>> warnings = validator.validate(entity);
        if (!warnings.isEmpty()) {
            RepositoryActionResult<E> result = createResult(null, ActionResultType.INVALID_PARAMS);
            for (ConstraintViolation<E> warn : warnings) {
                result.addMessage(ResponseMessageFactory.createMessageFieldLocalized(entity.getClass(), warn.getPropertyPath().toString(), warn.getMessage()));
            }
            return result;
        }

        try {
            entity = repository.save(entity);
            return createResult(entity, ActionResultType.CREATE);
        } catch (Exception e) {
            return repositoryExceptionHandler(entity, e);
        }
    }


    public <ID, E extends IEntity<ID>> RepositoryActionResult<E> findById(JpaRepository<E, ID> repository, ID id) {
        Optional<RepositoryActionResult<E>> checkResult = checkInvalidId(id);
        if (checkResult.isPresent()) return checkResult.get();

        try {
            Optional<E> find = repository.findById(id);
            if (find.isEmpty()) {
                return createResult(null, ActionResultType.NOT_FOUND);
            }
            return createResult(find.get(), ActionResultType.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return createResult(null, ActionResultType.SERVER_ERROR, ResponseMessageFactory.createMessageThrow(e));
        }
    }

    public <ID, E extends IEntity<ID>> RepositoryActionResult<E> update(JpaRepository<E, ID> repository, ID id, E entity) {
        if (!repository.existsById(id)) return createResult(null, ActionResultType.NOT_FOUND);

        return save(repository, entity);
    }

    public <ID, E extends IEntity<ID>> RepositoryActionResult<E> delete(JpaRepository<E, ID> repository, ID id) {
        Optional<RepositoryActionResult<E>> checkResult = checkInvalidId(id);
        if (checkResult.isPresent()) return checkResult.get();

        try {
            Optional<E> find = repository.findById(id);
            if (find.isEmpty()) {
                return createResult(null, ActionResultType.NOT_FOUND);
            }
            repository.delete(find.get());
            return createResult(find.get(), ActionResultType.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return createResult(null, ActionResultType.SERVER_ERROR, ResponseMessageFactory.createMessageThrow(e));
        }
    }

    public <ID, E extends IEntity<ID>> Optional<RepositoryActionResult<E>> checkInvalidId(ID id) {
        if (id == null)
            return Optional.of(createResult(null, ActionResultType.INVALID_PARAMS,
                    ResponseMessageFactory.createMessageFieldLocalized("id", "message.warn.field.empty")));

        if (id instanceof Number) {
            long idNumber = ((Number) id).longValue();
            if (idNumber <= 0)
                return Optional.of(createResult(null, ActionResultType.INVALID_PARAMS,
                        ResponseMessageFactory.createMessageFieldLocalized("id", "message.warn.field.id.invalid")));
        }

        return Optional.empty();
    }

    public static <E extends IEntity<?>> RepositoryActionResult<E> createResult(E data, ActionResultType type, IResponseMessage... messages) {
        RepositoryActionResult<E> result = new RepositoryActionResult<>(data, type);
        for (IResponseMessage message : messages) {
            result.addMessage(message);
        }
        return result;
    }

    private final static Pattern PATTERN_FIELD_NAME = Pattern.compile("\\(\\w*\\)");

    public <ID, E extends IEntity<ID>> RepositoryActionResult<E> repositoryExceptionHandler(E entity, Exception e) {
        if (!(e instanceof DataIntegrityViolationException)) {
            e.printStackTrace();
            return createResult(null, ActionResultType.SERVER_ERROR, ResponseMessageFactory.createMessageThrow(e));
        }
        Throwable cause = e.getCause();
        if (cause instanceof ConstraintViolationException exception) {
            String message = exception.getSQLException().getMessage();
            Matcher matcher = PATTERN_FIELD_NAME.matcher(message);
            if (matcher.find()) {
                String fieldName = matcher.group();
                fieldName = fieldName.substring(1, fieldName.length() - 1);

                return createResult(null, ActionResultType.INVALID_PARAMS,
                        ResponseMessageFactory.createMessageFieldLocalized(entity.getClass(), fieldName, "message.warn.field.duplicate"));
            }
        }
        e.printStackTrace();
        return createResult(null, ActionResultType.SERVER_ERROR, ResponseMessageFactory.createMessageThrow(e));
    }

}
