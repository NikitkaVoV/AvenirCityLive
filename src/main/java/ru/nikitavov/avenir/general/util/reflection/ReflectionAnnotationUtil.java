package ru.nikitavov.avenir.general.util.reflection;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ReflectionAnnotationUtil {

    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        ArrayList<Field> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                list.add(field);
            }
        }
        return list;
    }

    public static List<Class<?>> getClassesWithAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources;
            resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName, annotationClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) return classes;
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName(), annotationClass));
            } else if (file.getName().endsWith(".class")) {
                Class<?> cls = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (cls.isAnnotationPresent(annotationClass)) {
                    classes.add(cls);
                }
            }
        }
        return classes;
    }
}
