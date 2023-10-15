package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nikitavov.avenir.database.model.entity.Region;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT DISTINCT r.* FROM regions r WHERE (name ILIKE CONCAT('%', :nam, '%'))")
    Page<Region> findRegions(@Param("nam") String name,
                                         Pageable pageable);
}