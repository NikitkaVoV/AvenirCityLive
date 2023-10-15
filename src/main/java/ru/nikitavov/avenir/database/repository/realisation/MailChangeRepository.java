package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.avenir.database.model.security.mail.MailChange;
import ru.nikitavov.avenir.database.repository.interfaces.IMailEntityRepository;

public interface MailChangeRepository extends JpaRepository<MailChange, Integer>, IMailEntityRepository<MailChange> {
}