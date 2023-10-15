package ru.nikitavov.avenir.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.avenir.database.model.security.RedirectUrl;

import java.util.Optional;

public interface RedirectUrlRepository extends JpaRepository<RedirectUrl, Integer> {
    Optional<RedirectUrl> findByHash(String hash);
    Optional<RedirectUrl> findByUrl(String url);
}