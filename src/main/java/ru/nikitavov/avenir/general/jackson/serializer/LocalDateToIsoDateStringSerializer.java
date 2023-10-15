package ru.nikitavov.avenir.general.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateToIsoDateStringSerializer extends JsonSerializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String formattedDate = localDate.format(formatter);
        jsonGenerator.writeString(formattedDate);
    }
}