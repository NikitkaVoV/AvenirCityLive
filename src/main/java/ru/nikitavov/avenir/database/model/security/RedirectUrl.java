package ru.nikitavov.avenir.database.model.security;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "redirect_urls")
public class RedirectUrl {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "url", nullable = false, length = 512)
    String url;

    @NotBlank
    @Column(name = "url_hash", nullable = false)
    String hash;
}
