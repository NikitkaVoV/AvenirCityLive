package ru.nikitavov.avenir.general.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.nikitavov.avenir.general.jackson.deserializer.IsoDateStringToLocalDateDeserializer;
import ru.nikitavov.avenir.general.jackson.serializer.LocalDateToIsoDateStringSerializer;

import java.time.LocalDate;

@Configuration
public class JacksonConfig {

    @Primary
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateToIsoDateStringSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new IsoDateStringToLocalDateDeserializer());

        return builder.createXmlMapper(false)
                .modules(javaTimeModule)
                .build();
    }
}