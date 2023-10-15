package ru.nikitavov.avenir.general.util;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtil {
    public static List<File> filesInDirectory(URI directory, String fileNamePattern) {
        Pattern pattern = Pattern.compile(fileNamePattern);
        ArrayList<File> result = new ArrayList<>();
        File[] files = new File(directory).listFiles();
        if (files == null) return result;
        for (File file : files) {
            if (pattern.matcher(file.getName()).find()) {
                result.add(file);
            }
        }
        return result;
    }
}
