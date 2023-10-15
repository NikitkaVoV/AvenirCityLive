package ru.nikitavov.avenir.general.util.string;

import java.util.Locale;

public class StringUtil {
    public static boolean equalsIgnoreCase(String one, String two) {
        if (one == null && two == null) return true;
        if (one == null || two == null) return false;
        return one.toLowerCase(Locale.ROOT).equalsIgnoreCase(two);
    }
}
