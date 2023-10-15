package ru.nikitavov.avenir.database.model.base;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;
import ru.nikitavov.avenir.database.model.security.Permission;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements IEntity<Integer> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Column(name = "uuid", nullable = false)
    UUID uuid;

    @Builder.Default
    @Column(name = "surname")
    String surname = "";

    @Builder.Default
    @Column(name = "name")
    String name = "";

    @Builder.Default
    @Column(name = "patronymic")
    String patronymic = "";

    @Builder.Default
    @Email
    @Column(name = "email")
    String email = "";

    @Column(name = "password")
    String password;

    @Builder.Default
    @NotNull
    @Column(name = "disabled", nullable = false)
    Boolean disabled = false;

    @Builder.Default
    @NotNull
    @Column(name = "blocked", nullable = false)
    boolean blocked = false;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "assigned_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    final Set<Role> roles = new LinkedHashSet<>();

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "assigned_permissions",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
    )
    final Set<Permission> permissions = new LinkedHashSet<>();

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @OneToMany
    @JoinColumn(name = "user_id")
    final Set<LinkedSocialNetwork> linkedSocialNetworks = new HashSet<>();
}
