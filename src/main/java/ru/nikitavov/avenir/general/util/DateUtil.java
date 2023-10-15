package ru.nikitavov.avenir.general.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static DateTimeFormatter buildDateTimeFormatter(String format) {
        String dateStr = "17.05.2022";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();

        return DateTimeFormatter.ofPattern(format);
    }

    public static LocalDate textTimeToLocalDate(String dateText, String format) {
        return LocalDate.parse(dateText, buildDateTimeFormatter(format));
    }

    public static ZonedDateTime textToZonedDateTime(String dateText, String format) {
        return textTimeToLocalDate(dateText, format).atStartOfDay(ZoneId.systemDefault());
    }

    public static Instant textToInstant(String dateText, String format) {
        return textToZonedDateTime(dateText, format).toInstant();
    }

    public static Instant localDateToInstantOfStartDay(LocalDate date) {
//        LocalDateTime dateTime = date.atStartOfDay();
        ZoneOffset offset = ZoneOffset.UTC; // UTC смещение
        return date.atStartOfDay(offset).toInstant();
//        return dateTime.toInstant(ZoneId.systemDefault().getRules().getOffset(dateTime));
    }

    public static boolean isTimeUp(Date date) {
        return System.currentTimeMillis() > date.getTime();
    }

    public static Date createExpiryDate(long extraMilliseconds) {
        return new Date(System.currentTimeMillis() + extraMilliseconds);
    }
}
