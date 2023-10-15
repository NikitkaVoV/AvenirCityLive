package ru.nikitavov.avenir.general.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollectionUtil {

    public static <E> List<E> asList(E... elements) {
        var list = new ArrayList<E>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }

    public static <E> void insertArrayToList(List<E> list, E[] elements) {
        list.addAll(Arrays.asList(elements));
    }

    public static <E> void insertListToList(List<E> list, List<E> elements) {
        list.addAll(elements);
    }

    public static void clearOfNull(List<?> list) {
        list.removeIf(Objects::isNull);
    }

    public static <E> Set<E> mergeSets(Set<E>... sets) {
        HashSet<E> result = new HashSet<>();

        for (Set<E> set : sets) {
            result.addAll(set);
        }

        return result;
    }

    public static <K, E> Optional<E> findInMap(Map<K, E> map, K key) {
        return Optional.ofNullable(map.get(key));
    }

    public static <T> List<List<T>> splitList(List<T> sourceList, int chunkSize) {
        return IntStream.range(0, (sourceList.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> sourceList.subList(i * chunkSize, Math.min((i + 1) * chunkSize, sourceList.size())))
                .collect(Collectors.toList());
    }

    public static <K, V> Map<K, V> createMap(K key, V value) {
        HashMap<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
