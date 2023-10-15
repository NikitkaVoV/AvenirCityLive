package ru.nikitavov.avenir.database.model.base;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "linked_social_networks")
public class LinkedSocialNetwork implements IEntity<Integer> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @NotNull
    @Column(name = "social_network_type", nullable = false)
    @Enumerated(EnumType.STRING)
    SocialNetworkType socialNetworkType;

    @NotBlank
    @Column(name = "user_social_network_id", nullable = false)
    String userSocialNetworkId;

    @Column(name = "additional_information")
    String additionalInformation;

    @Builder.Default
    @NotNull
    @Column(name = "enabled_mailing", nullable = false)
    Boolean enabledMailing = true;
}
