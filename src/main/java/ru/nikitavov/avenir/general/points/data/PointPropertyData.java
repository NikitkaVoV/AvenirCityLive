package ru.nikitavov.avenir.general.points.data;

import java.math.BigDecimal;
import java.util.List;

public record PointPropertyData(String name, String formatted, List<String> categories, BigDecimal lat, BigDecimal lon) {
}
