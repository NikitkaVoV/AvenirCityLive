package ru.nikitavov.avenir.general.util.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringFormat {
    private static final Pattern PATTERN =  Pattern.compile("[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+");
    public static String convert2CamelCase(String str) {
        Matcher matcher = PATTERN.matcher(str);
        List< String > matched = new ArrayList< >();
        while (matcher.find()) {
            matched.add(matcher.group(0));
        }
        String camelcase = matched.stream()
                .map(x -> x.substring(0, 1).toUpperCase() + x.substring(1).toLowerCase())
                .collect(Collectors.joining());
        return camelcase.substring(0, 1).toLowerCase() + camelcase.substring(1);
    }

    public static String convert2SnakeCase(String input) {
        Matcher matcher = PATTERN.matcher(input);
        List < String > matched = new ArrayList < > ();
        while (matcher.find()) {
            matched.add(matcher.group(0));
        }
        return matched.stream()
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"));
    }
}
