package com.totvs.challenge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class BaseTest {

    protected ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    private String readResourceFile(String path) {
        var file = new ClassPathResource(path).getFile();
        return Files.readString(Paths.get(file.getPath()));
    }

    @SneakyThrows
    protected <T> T createObjectFromClass(String path, Class<T> object) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.readValue(readResourceFile(path), object);
    }

}
