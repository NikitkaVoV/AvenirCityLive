package ru.nikitavov.avenir.general.points;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nikitavov.avenir.general.model.tuple.Tuple2;
import ru.nikitavov.avenir.general.points.data.PointData;
import ru.nikitavov.avenir.general.points.data.PointsData;
import ru.nikitavov.avenir.general.points.data.RegionAnaliseResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPointsDataService {

    private final String baseUrl = "https://api.geoapify.com/v2/places";
    private final String apiKey = "c5a37b59b6764e37a0f9d0848dd1470f";

    private final RestTemplate restTemplate = new RestTemplate();

    public RegionAnaliseResult regionAnalyzer(String placeHash) {
        List<PointData> pointsPositive = getPoints(getPositiveCategorises(), "place:", placeHash);
        List<PointData> pointsNegative = getPoints(getNegativeCategorises(), "place:", placeHash);

        return getResult(pointsPositive, pointsNegative);
    }

    public RegionAnaliseResult regionAnalyzer(Tuple2<List<PointData>, List<PointData>> points) {
        return getResult(points.param1(), points.param2());
    }

    private RegionAnaliseResult getResult(List<PointData> pointsPositive, List<PointData> pointsNegative) {
        double percent = (double) pointsPositive.size() / (pointsPositive.size() + pointsNegative.size());

        boolean normal = percent >= 0.5d;

        List<PointData> invalidPoints = new ArrayList<>();

        for (PointData point : pointsNegative) {
            if (!checkNegativePoint(point, pointsPositive, 100)) {
                invalidPoints.add(point);
            }
        }

        BigDecimal scope = calculateSum(pointsPositive, pointsNegative);
        List<PointData> prohibitedPoints = getProhibitedPoints(pointsPositive, pointsNegative);
        return new RegionAnaliseResult(normal, scope, prohibitedPoints, invalidPoints);
    }

    public boolean checkNegativePoint(PointData negativePoint, List<PointData> pointsPositive, int meters) {
        for (PointData positivePoint : pointsPositive) {
            BigDecimal haversine = haversine(
                    negativePoint.properties().lat(),
                    negativePoint.properties().lon(),
                    positivePoint.properties().lat(),
                    positivePoint.properties().lon()
            );

            if (haversine.toBigInteger().intValue() <= meters) {
                return true;
            }
        }
        return false;
    }

    private static final BigDecimal EARTH_RADIUS = BigDecimal.valueOf(6371000);

    public static BigDecimal haversine(BigDecimal let1, BigDecimal lon1, BigDecimal let2, BigDecimal lon2) {
        BigDecimal degreesToRadians = BigDecimal.valueOf(Math.PI / 180);
        BigDecimal dLat = let2.subtract(let1).multiply(degreesToRadians);
        BigDecimal dLon = lon2.subtract(lon1).multiply(degreesToRadians);

        BigDecimal a = new BigDecimal(Math.sin(dLat.divide(BigDecimal.valueOf(2)).doubleValue()))
                .multiply(new BigDecimal(Math.sin(dLat.divide(BigDecimal.valueOf(2)).doubleValue())))
                .add(new BigDecimal(Math.cos(let1.multiply(degreesToRadians).doubleValue()))
                        .multiply(new BigDecimal(Math.cos(let2.multiply(degreesToRadians).doubleValue()))
                                .multiply(new BigDecimal(Math.sin(dLon.divide(BigDecimal.valueOf(2)).doubleValue()))
                                        .multiply(new BigDecimal(Math.sin(dLon.divide(BigDecimal.valueOf(2)).doubleValue()))))));

        BigDecimal c = new BigDecimal(2)
                .multiply(new BigDecimal(Math.atan2(Math.sqrt(a.doubleValue()), Math.sqrt(1 - a.doubleValue()))));

        return EARTH_RADIUS.multiply(c);
    }

    public List<String> getPositiveCategorises() {
        return Arrays.asList(
                "commercial.health_and_beauty",
                " commercial.food_and_drink",
                "commercial.outdoor_and_sport",
                "education",
                "commercial.food_and_drink.farm",
                "childcare.kindergarten",
                "entertainment.culture",
                "healthcare",
                "leisure",
                "national_park",
                "building.university",
                "building.college",
                "building.kindergarten",
                "building.school",
                "building.sport",
                "heritage",
                "building.spa",
                "activity.sport_club",
                "sport"
        );
    }

    public List<String> getNegativeCategorises() {
        return Arrays.asList(
                "commercial.smoking",
                "commercial.weapons",
                "catering.bar",
                "catering.fast_food",
                "catering.pub",
                "catering.taproom",
                "adult",
                "building.prison",
                "production.brewery",
                "production.winery"
        );
    }

    public List<PointData> getPoints(List<String> categories, String filterType, String filter) {
        String apiUrl = baseUrl +
                "?categories=" +
                String.join(",", categories) +
                "&filter=" + filterType + ":" +
                filter +
                "&lang=ru&limit=500&apiKey=" +
                apiKey;

        ResponseEntity<PointsData> responseEntity = restTemplate.getForEntity(apiUrl, PointsData.class);

        return responseEntity.getBody().features();
    }

    public BigDecimal calculateSum(List<PointData> pointsPositive, List<PointData> pointsNegative) {
        return calculatePositive(pointsPositive).min(calculateNegative(pointsNegative));
    }

    public BigDecimal calculatePositive(List<PointData> pointsPositive) {
        return new BigDecimal(pointsPositive.size());
    }

    public BigDecimal calculateNegative(List<PointData> pointsNegative) {
        return new BigDecimal(pointsNegative.size());
    }

    public List<PointData> getProhibitedPoints(List<PointData> pointsPositive, List<PointData> pointsNegative) {
        List<PointData> pointsNegativeFilter = pointsNegative.stream().filter(pointData ->
                pointData.properties().categories().contains("commercial.smoking")
        ).toList();

        List<PointData> points = new ArrayList<>();

        List<PointData> pointsPositiveFilter = pointsPositive.stream().filter(pointData -> pointData.properties().categories().contains("education") || pointData.properties().categories().contains("childcare.kindergarten")).toList();
        for (PointData pointNegative : pointsNegativeFilter) {
            if (checkNegativePoint(pointNegative, pointsPositiveFilter, 100)) {
                points.add(pointNegative);
            }

        }

        pointsNegativeFilter = pointsNegative.stream().filter(pointData ->
                pointData.properties().categories().contains("catering.taproom") ||
                        pointData.properties().categories().contains("production.winery") ||
                        pointData.properties().categories().contains("production.brewery")
        ).toList();
        for (PointData pointNegative : pointsNegativeFilter) {
            if (checkNegativePoint(pointNegative, pointsPositiveFilter, 70)) {
                points.add(pointNegative);
            }

        }


        return points;
    }
}
