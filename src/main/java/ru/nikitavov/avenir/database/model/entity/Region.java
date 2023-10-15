package ru.nikitavov.avenir.database.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.avenir.database.model.IEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "regions")
public class Region implements IEntity<Integer> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank
    @Column(name = "name", nullable = false)
    String name;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "region_map_points",
            joinColumns = {@JoinColumn(name = "region_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "map_point_id", referencedColumnName = "id")}
    )
    final List<MapPoint> points = new ArrayList<>();
}
