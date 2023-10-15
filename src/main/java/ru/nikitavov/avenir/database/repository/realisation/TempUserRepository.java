package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.avenir.database.model.security.mail.TempUser;
import ru.nikitavov.avenir.database.repository.interfaces.IMailEntityRepository;

public interface TempUserRepository extends JpaRepository<TempUser, Integer>, IMailEntityRepository<TempUser> {
}