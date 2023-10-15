package ru.nikitavov.avenir.general.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DoubleConverter implements Converter<String, Double> {
    private static final Pattern NUMBER_PATTER = Pattern.compile("[^\\d,. ]");

    @Override
    public Double convert(String source) {
        if (NUMBER_PATTER.matcher(source).find()) throw new NumberFormatException();
        if (source.contains(",")) {
            source = source.replace(',', '.');
        }

        return Double.parseDouble(source);
    }
}
