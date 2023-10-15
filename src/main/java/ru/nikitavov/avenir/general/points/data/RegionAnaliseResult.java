package ru.nikitavov.avenir.general.points.data;

import java.math.BigDecimal;
import java.util.List;

public record RegionAnaliseResult(boolean isNormalRegion, BigDecimal scope, List<PointData> prohibitedPoints,
                                  List<PointData> invalidPoints) {

}
