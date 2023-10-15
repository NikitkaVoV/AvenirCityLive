package ru.nikitavov.avenir.general.points;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.general.points.data.PointCoordinate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolygonService {

    public final GeometryFactory geometryFactory = new GeometryFactory();

    public double calculateArea(Geometry polygon) {
        Envelope envelope = polygon.getEnvelopeInternal();

        double areaInSquareDegrees = envelope.getArea();

        double metersPerDegree = 111_319.9;

        double areaInSquareMeters = areaInSquareDegrees * metersPerDegree * metersPerDegree;

        System.out.println("Площадь многоугольника в метрах квадратных: " + areaInSquareMeters + " м²");

        return areaInSquareMeters;
    }

    public Geometry createPolygon(List<PointCoordinate> points) {
        WKTReader wktReader = new WKTReader(geometryFactory);

        String polygonBody = points.stream().map(p -> p.lon() + " " + p.lat()).collect(Collectors.joining(","));
        PointCoordinate zeroPoint = points.get(0);
        String startPoint = zeroPoint.lon() + " " + zeroPoint.lat();
        String polygonRaw = "POLYGON ((" + polygonBody + "," + startPoint + "))";
        try {
            return wktReader.read(polygonRaw);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkInside(Geometry polygon, PointCoordinate checkPoint) {
        Coordinate pointCoordinate = new Coordinate(checkPoint.lon(), checkPoint.lat());
        Geometry point = geometryFactory.createPoint(pointCoordinate);
        return polygon.contains(point);
    }
}
