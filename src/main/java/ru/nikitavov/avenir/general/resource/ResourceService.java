package ru.nikitavov.avenir.general.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class ResourceService {

    private ResourcePatternResolver resourcePatternResolver;

    public Optional<InputStream> getResourceStream(String path) {
        try {
            Resource resource = resourcePatternResolver.getResource(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + path);

            if (resource.isReadable()) {
                return Optional.of(resource.getInputStream());
            }
        } catch (Exception e) {
            log.error("Error when reading a file on the path: " + path, e);
        }
        return Optional.empty();
    }

    public List<InputStream> getResourceStreams(String pathPattern) {
        ArrayList<InputStream> result = new ArrayList<>();

        try {
            for (Resource resource : resourcePatternResolver.getResources(pathPattern)) {
                if (!resource.isReadable()) continue;
                result.add(resource.getInputStream());
            }
        } catch (IOException e) {
            log.error("Error when reading a files on the path: " + pathPattern, e);
        }

        return result;
    }
}
