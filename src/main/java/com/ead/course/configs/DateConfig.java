package com.ead.course.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;
// Como está comentado ele não leva em consideração essa abordagem a nível de classe, todas as classes estaria
// ultilizando esse padão para data.
//@Configuration
public class DateConfig {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter
            .ofPattern(DATETIME_FORMAT));

    @Bean
    // Para o Spring levar em consideração essas configurações quando subir a aplicação
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return new ObjectMapper().registerModule(module);
    }

}
