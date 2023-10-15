package ru.nikitavov.avenir.database.model.security.mail;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "temp_users")
public class TempUser implements IEntity<Integer>, IMailEntity {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Email
    @Column(name = "email", nullable = false)
    String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    String password;

    @NotBlank
    @Column(name = "token", nullable = false)
    String token;

    @Column(name = "expiry_date", nullable = false)
    Date expiryDate;

    @NotBlank
    @Column(name = "redirect_url", nullable = false, length = 500)
    String redirectUrl;

}
