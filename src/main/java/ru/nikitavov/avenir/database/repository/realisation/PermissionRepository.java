package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.avenir.database.model.security.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}