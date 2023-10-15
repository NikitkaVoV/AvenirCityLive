package ru.nikitavov.avenir.general.points;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.database.model.entity.MapPoint;
import ru.nikitavov.avenir.database.model.entity.Region;
import ru.nikitavov.avenir.database.repository.realisation.MapPointRepository;
import ru.nikitavov.avenir.database.repository.realisation.RegionRepository;
import ru.nikitavov.avenir.general.util.resource.ResourceUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final MapPointRepository mapPointRepository;
    private final ObjectMapper objectMapper;

    public void parseFile() throws Exception {
        if (regionRepository.count() != 0) {
            return;
        }
        Resource data = ResourceUtil.getResources("data", null).get(0);
        InputStream inputStream = data.getInputStream();
        byte[] fileBytes = IOUtils.toByteArray(inputStream);

        Data o = objectMapper.readValue(fileBytes, Data.class);
        o.altayskiyKray.forEach((name, data1) -> {
            List<MapPoint> points = data1.get("0").stream().map(doubles -> MapPoint.builder().lat(doubles.get(0)).lon(doubles.get(1)).build()).toList();
            points = mapPointRepository.saveAll(points);
            Region region = Region.builder().points(points).name(name).build();
            regionRepository.save(region);
        });
    }

    @Getter
    static class Data {
        private Map<String, Map<String, List<List<BigDecimal>>> > altayskiyKray;

        @JsonAnySetter
        public void setAltayskiyKray(String key, Map<String, List<List<BigDecimal>>> value) {
            if (altayskiyKray == null) {
                altayskiyKray = new HashMap<>();
            }
            altayskiyKray.put(key, value);
        }

    }

}
