package ru.nikitavov.avenir.database.model.base;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login_histories")
public class LoginHistory implements IEntity<Integer> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @NotNull
    @Column(name = "date_and_time", nullable = false)
    Instant date;
}
