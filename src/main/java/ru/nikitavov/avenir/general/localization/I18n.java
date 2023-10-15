package ru.nikitavov.avenir.general.localization;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import ru.nikitavov.avenir.general.localization.annotation.Localized;
import ru.nikitavov.avenir.general.localization.annotation.LocalizedField;
import ru.nikitavov.avenir.general.util.StringFormat;
import ru.nikitavov.avenir.general.util.reflection.ReflectionAnnotationUtil;
import ru.nikitavov.avenir.general.util.resource.ResourceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@Log4j2
public class I18n {

    private static final HashMap<Class<?>, HashMap<String, LangElement>> localizationClassFields = new HashMap<>();
    private static final HashMap<String, LangElement> keyLocalizations = new HashMap<>();
    private static final HashMap<Class<?>, HashMap<String, String>> fieldAllies = new HashMap<>();

    public static boolean contain(String key) {
        return keyLocalizations.containsKey(key);
    }

    public static String format(String key, Object... params) {
        LangElement element = keyLocalizations.get(key);
        if (element == null) return key;
        return element.find(Languages.RUSSIA).formatted(params);
    }

    /**
     * @param clazz           класс сущьностости для которой необходимо локализировать название поля
     * @param field           название поле котрое необходимо локализировать
     * @param isOrigFieldName если да, то будет искаться специальное название для поля
     * @return Локализированное название поля для конкретной сущьности
     */
    public static String fieldFormat(Class<?> clazz, String field, boolean isOrigFieldName) {
        try {
            if (isOrigFieldName) {
                HashMap<String, String> map = fieldAllies.get(clazz);
                if (map == null) return classAndFieldToKey(clazz, field);

                String allies = map.get(field);
                if (allies == null) return classAndFieldToKey(clazz, field);

                field = allies;
            }
            HashMap<String, LangElement> map = localizationClassFields.get(clazz);
            if (map == null) return classAndFieldToKey(clazz, field);

            LangElement element = map.get(field);
            if (element == null) return classAndFieldToKey(clazz, field);

            return element.find(Languages.RUSSIA);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static void init() {
        initLangs();
        initEntities();
    }

    private static void initLangs() {

        List<Resource> resources = ResourceUtil.getResources("lang", null);
        if (resources.size() == 0) return;
        for (Resource resource : resources) {
            List<String> lines;
            try {
                InputStream inputStream = resource.getInputStream();
                lines = ResourceUtil.getContext(inputStream);
                inputStream.close();
            } catch (IOException e) {
                log.error("Error when reading a file: " + resource.getFilename(), e);
                throw new RuntimeException(e);
            }

            for (String line : lines) {
                line = line.trim();
                if (line.isBlank() || line.startsWith("#")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length != 2) continue;
                LangElement element = keyLocalizations.computeIfAbsent(parts[0], s -> new LangElement());
                Languages language = Languages.findByFileName(resource.getFilename().split("\\.")[0]);
                element.put(language, parts[1]);
            }
        }

//        URI lang = ResourceUtil.getFile("schedule/lang");
//        if (lang == null) return;
//        List<File> files = FileUtil.filesInDirectory(lang, ".*lang$");
//        if (files.size() == 0) return;
//
//        for (File file : files) {
//            List<String> lines;
//            try {
//                lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//
//            for (String line : lines) {
//                line = line.trim();
//                if (line.isBlank() || line.startsWith("#")) continue;
//
//                String[] parts = line.split("=", 2);
//                if (parts.length != 2) continue;
//                LangElement element = keyLocalizations.computeIfAbsent(parts[0], s -> new LangElement());
//                Languages language = Languages.findByFileName(file.getName().split("\\.")[0]);
//                element.put(language, parts[1]);
//            }
//        }
    }

    private static void initEntities() {
        List<Class<?>> classes = ReflectionAnnotationUtil.getClassesWithAnnotation("ru.nikitavov.schedule.api", Localized.class);
        for (Class<?> clazz : classes) {
            for (Field field : ReflectionAnnotationUtil.getFieldsWithAnnotation(clazz, LocalizedField.class)) {
                String fieldKey = field.getAnnotation(LocalizedField.class).allies();
                String key = classAndFieldToKey(clazz, fieldKey);
                HashMap<String, String> alliess = fieldAllies.computeIfAbsent(clazz, aClass -> new HashMap<>());
                alliess.put(field.getName(), fieldKey);
                LangElement element = keyLocalizations.get(key);
                if (element == null) {
                    System.out.println("Key not found: '" + key + "'");
                    continue;
                }
                HashMap<String, LangElement> map = localizationClassFields.computeIfAbsent(clazz, aClass -> new HashMap<>());
                map.put(fieldKey, element);
            }
        }
    }

    private static String classAndFieldToKey(Class<?> clazz, String field) {
        StringBuilder builder = new StringBuilder();
        String className = StringFormat.convert2SnakeCase(clazz.getSimpleName());
        builder.append("entity").append(".").append(className).append(".").append(field);
        return builder.toString();
    }
}
