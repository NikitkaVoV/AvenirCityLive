package ru.nikitavov.avenir.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitavov.avenir.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;

import java.util.Optional;

@Repository
public interface LinkedSocialNetworkRepository extends JpaRepository<LinkedSocialNetwork, Integer> {
    Optional<LinkedSocialNetwork> findByUserSocialNetworkIdAndSocialNetworkType(String userSocialNetworkId, SocialNetworkType socialNetworkType);

    Optional<LinkedSocialNetwork> findByUser_IdAndSocialNetworkType(Integer id, SocialNetworkType socialNetworkType);

    long countByUser_Id(Integer id);

}