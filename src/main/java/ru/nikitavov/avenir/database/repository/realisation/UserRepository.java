package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nikitavov.avenir.database.model.base.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    @Query(nativeQuery = true, value = """
                SELECT DISTINCT u.* FROM users u JOIN assigned_roles a ON u.id = a.user_id
                WHERE (CAST(uuid AS text) ILIKE CONCAT('%', :uuid, '%'))
                   OR (name ILIKE CONCAT('%', :name, '%') AND
                       surname ILIKE CONCAT('%', :surname, '%') AND
                       patronymic ILIKE CONCAT('%', :patronymic, '%'))
            """)
    Page<User> findUsersByUuidOrFullname(@Param("uuid") String uuid, @Param("name") String name,
                                         @Param("surname") String surname, @Param("patronymic") String patronymic,
                                         Pageable pageable);


}