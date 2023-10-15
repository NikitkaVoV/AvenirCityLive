package ru.nikitavov.avenir.database.repository.interfaces;

import ru.nikitavov.avenir.database.model.security.mail.IMailEntity;

import java.util.Optional;

public interface IMailEntityRepository<E extends IMailEntity> {
    Optional<E> findByToken(String token);

}
