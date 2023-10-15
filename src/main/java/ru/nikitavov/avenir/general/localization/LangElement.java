package ru.nikitavov.avenir.general.localization;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;

@Data
public class LangElement {
    @Getter(AccessLevel.NONE)
    private final HashMap<Languages, String> localizations = new HashMap<>();

    public void put(Languages language, String localization) {
        localizations.put(language, localization);
    }

    public String find(Languages language) {
        String localization = localizations.get(language);
        if (localization == null) {
            localization = localizations.get(Languages.getDefault());
        }
        return localization;
    }
}
