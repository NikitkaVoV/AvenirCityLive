package ru.nikitavov.avenir.web.security.data;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;

@RequestScope
@Component
public class RequestStorage {

    private final HashMap<String, Object> storage = new HashMap<>();

    public void add(String key, Object item) {
        storage.put(key, item);
    }

    public boolean contain(String key) {
        return storage.containsKey(key);
    }

    public <T> T remove(String key) {
        return (T) storage.remove(key);
    }
}
