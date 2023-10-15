package ru.nikitavov.avenir.general.util;

import java.util.regex.Pattern;

public class EmailUtil {
    public static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    public static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
    public static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
    public static final String PATTERN = "^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN + "|" + IP_DOMAIN + ")$";

    public static final Pattern PATTERN_REGEX = Pattern.compile(PATTERN);

    public static boolean isEmail(String text) {
        return PATTERN_REGEX.matcher(text).matches();
    }
}
