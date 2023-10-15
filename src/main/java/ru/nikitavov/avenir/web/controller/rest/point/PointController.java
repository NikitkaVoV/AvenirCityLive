package ru.nikitavov.avenir.web.controller.rest.point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.avenir.general.model.tuple.Tuple2;
import ru.nikitavov.avenir.general.points.GetPointsDataService;
import ru.nikitavov.avenir.general.points.PolygonService;
import ru.nikitavov.avenir.general.points.data.*;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("point")
public class PointController {

    private final GetPointsDataService pointsDataService;
    private final PolygonService polygonService;
    private final ObjectMapper objectMapper;

    @GetMapping("/me")
    public ResponseEntity<RegionAnaliseResult> check(@RequestParam String hash) {
        RegionAnaliseResult result = pointsDataService.regionAnalyzer(hash);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/type")
    public ResponseEntity<MessageWrapper<RegionDataResponse>> check2(@RequestParam PointJsonRequest request) throws JsonProcessingException {
        List<PointCoordinate> points = objectMapper.readValue(request.json(), new TypeReference<>() {
        });
        Geometry polygon = polygonService.createPolygon(points);
        Tuple2<List<PointData>, List<PointData>> data = getData(points, polygon);
        RegionDataResponse response = getStatValueByType(data.param1(), data.param2(), polygon);

        return ResponseEntity.ok(new MessageWrapper<>(response));
    }

    @PostMapping("/algorithm")
    public ResponseEntity<MessageWrapper<RegionAnaliseResult>> algorithm(@RequestBody PointJsonRequest request) throws JsonProcessingException {
        List<PointCoordinate> points = objectMapper.readValue(request.json(), new TypeReference<>() {
        });
        Geometry polygon = polygonService.createPolygon(points);
        Tuple2<List<PointData>, List<PointData>> data = getData(points, polygon);
        RegionAnaliseResult result = pointsDataService.regionAnalyzer(data);

        return ResponseEntity.ok(new MessageWrapper<>(result));
    }

    @PostMapping("/search_points")
    public ResponseEntity<MessageWrapper<SearchPointsResponse>> check3(@RequestBody SearchPointsRequest request) throws JsonProcessingException {
        List<PointCoordinate> points = objectMapper.readValue(request.json(), new TypeReference<>() {
        });
        Geometry polygon = polygonService.createPolygon(points);
        Tuple2<List<PointData>, List<PointData>> data = getData(points, polygon);
        List<PointData> list = Stream.concat(data.param1().stream(), data.param2().stream())
                .filter(pointData -> pointData.properties().categories().contains(request.category()))
                .toList();

        return ResponseEntity.ok(new MessageWrapper<>(new SearchPointsResponse(list)));
    }

    private final HashMap<Integer, Tuple2<List<PointData>, List<PointData>>> cacheData = new HashMap<>();

    private Tuple2<List<PointData>, List<PointData>> getData(List<PointCoordinate> points, Geometry polygon) {
        int hash = points.hashCode();
        Tuple2<List<PointData>, List<PointData>> data;
        if (cacheData.containsKey(hash)) {
            data = cacheData.get(hash);
        } else {
            Envelope envelope = polygon.getEnvelopeInternal();

            String filter = envelope.getMinX() + "," + envelope.getMinY() + "," + envelope.getMaxX() + "," + envelope.getMaxY();

            List<PointData> positives = pointsDataService.getPoints(pointsDataService.getPositiveCategorises(), "rect", filter);
            List<PointData> negatives = pointsDataService.getPoints(pointsDataService.getNegativeCategorises(), "rect", filter);

            positives.removeIf(pointData -> !polygon.contains(polygonService.geometryFactory.createPoint(
                    new Coordinate(pointData.properties().lon().doubleValue(), pointData.properties().lat().doubleValue()))
            ));

            data = new Tuple2<>(positives, negatives);
            cacheData.put(hash, data);
        }

        return data;
    }

    public RegionDataResponse getStatValueByType(List<PointData> positives, List<PointData> negatives, Geometry polygon) {
        int count = positives.size() + negatives.size();
        double area = polygonService.calculateArea(polygon);
        HashMap<String, Double> amounts = new HashMap<>();
        amounts.put(String.valueOf(StateType.GAS), count * 39 + (count * 39 * 0.1));
        amounts.put(String.valueOf(StateType.WATER), count * 111 + (count * 111 * 0.1));
        amounts.put(String.valueOf(StateType.ENERGY), count * 124 + (count * 124 * 0.1));

        return new RegionDataResponse(area, amounts);
    }

}
