package ru.nikitavov.avenir.database.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "map_points")
public class MapPoint implements IEntity<Integer> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "lat", nullable = false, scale = 10)
    BigDecimal lat;

    @Column(name = "lon", nullable = false, scale = 10)
    BigDecimal lon;

}
