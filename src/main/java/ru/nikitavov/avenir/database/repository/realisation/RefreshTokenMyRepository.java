package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nikitavov.avenir.database.model.security.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenMyRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findFirstByRefreshTokenHash(String token);

    Optional<RefreshToken> findFirstByAccessTokenHash(String token);

    @Query( "select r from RefreshToken r where r.user.id = ?1 order by r.id desc")
    List<RefreshToken> getUserTokens(Integer id);
}