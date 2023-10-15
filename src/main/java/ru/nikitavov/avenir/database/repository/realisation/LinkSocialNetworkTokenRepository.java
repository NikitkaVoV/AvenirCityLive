package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.avenir.database.model.security.LinkSocialNetworkToken;

import java.util.Optional;

public interface LinkSocialNetworkTokenRepository extends JpaRepository<LinkSocialNetworkToken, Integer> {

    Optional<LinkSocialNetworkToken> findByToken(String token);

    void deleteByUser_Id(Integer id);
}