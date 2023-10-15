package ru.nikitavov.avenir.general.localization;

import lombok.Getter;
import ru.nikitavov.avenir.general.util.StringUtil;

@Getter
public enum Languages {
    RUSSIA("ru", "ru_ru");

    private final String codeLang;
    private final String fileName;

    Languages(String codeLang, String fileName) {
        this.codeLang = codeLang;
        this.fileName = fileName;
    }

    public static Languages findByCode(String code) {
        for (Languages value : values()) {
            if (StringUtil.equalsIgnoreCase(value.codeLang, code)) {
                return value;
            }
        }
        return RUSSIA;
    }
    public static Languages findByFileName(String fileName) {
        for (Languages value : values()) {
            if (StringUtil.equalsIgnoreCase(value.fileName, fileName)) {
                return value;
            }
        }
        return RUSSIA;
    }

    public static Languages getDefault() {
        return RUSSIA;
    }
}
