package ru.nikitavov.avenir.general.util.resource;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Log4j2
public class ResourceUtil {

    public static List<URI> getFiles(String path) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            ArrayList<URI> uris = new ArrayList<>();
            Iterator<URL> urlIterator = classloader.getResources(path).asIterator();
            urlIterator.forEachRemaining(url -> {
                try {
                    uris.add(url.toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            return uris;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static URI getFile(String path) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            URL lang = classloader.getResource(path);
            if (lang == null) return null;
            return lang.toURI();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public static List<String> getContext(InputStream stream) {
        byte[] fileData;
        try {
            fileData = FileCopyUtils.copyToByteArray(stream);
        } catch (IOException e) {
            log.error("Error when reading the context stream", e);
            return new ArrayList<>();
        }
        String fileContent = new String(fileData, StandardCharsets.UTF_8);

        return Arrays.stream(fileContent.split("\n")).toList();
    }

    public static List<Resource> getResources(String directory, Pattern filePattern) {
        ArrayList<Resource> result = new ArrayList<>();

        Resource[] resources;
        try {
            resources = resolver.getResources("classpath:" + directory + "/*");
        } catch (IOException e) {
            log.error("Error when reading a files on the path: " + directory, e);
            return new ArrayList<>();
        }

        for (Resource resource : resources) {
            if (!resource.isReadable()) {
                log.warn("Unable to read the file: " + resource.getFilename());
            }

            result.add(resource);
        }

        return result;
    }
}
