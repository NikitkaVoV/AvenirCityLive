package ru.nikitavov.avenir.database.model.security;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.database.model.base.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "link_social_network_tokens")
public class LinkSocialNetworkToken implements IEntity<Integer> {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "token")
    private String token;
}