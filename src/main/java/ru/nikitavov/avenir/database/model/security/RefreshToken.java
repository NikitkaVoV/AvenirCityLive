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
@Table(name = "refresh_tokens")
public class RefreshToken implements IEntity<Integer> {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "access_expiry_date", nullable = false)
    Date accessExpiryDate;

    @Column(name = "refresh_expiry_date", nullable = false)
    Date refreshExpiryDate;

    @Column(name = "access_token_hash", nullable = false)
    String accessTokenHash;

    @Column(name = "refresh_token_hash", nullable = false)
    String refreshTokenHash;
}