package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitavov.avenir.database.model.base.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
}