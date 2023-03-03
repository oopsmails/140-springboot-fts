package com.oopsmails.lucenesearch.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class AlbertJsonUtil {

    public static ObjectMapper getObjectMapper() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static String objectToJsonString(Object obj, boolean beautify) {
        String result = "";
        try {
            result = beautify ?
                    getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj) :
                    getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("logging object error, obj: [{}]", obj);
            log.warn("logging object error, exception will be ignored:[{}] ", e.getMessage());
        }
        log.trace("- logging object: [{}]", result);
        return result;

    }

    public static <T> T jsonFileToObject(String fileNameWithPath, Class<T> type) {
        T target = null;
        try {
            File jsonFile = new File(fileNameWithPath);
            target = getObjectMapper().readValue(jsonFile, type);
        } catch (Exception e) {
            log.warn("logging object error, obj: [{}]", e.getMessage());
        }

        return target;
    }

    public static <T> T jsonFileToObject(String fileNameWithPath, TypeReference<T> type) {
        T target = null;
        try {
            File jsonFile = new File(fileNameWithPath);
            target = getObjectMapper().readValue(jsonFile, type);
        } catch (Exception e) {
            log.warn("logging object error, obj: [{}]", e.getMessage());
        }

        return target;
    }

    public static <T> T jsonToObject(String json, Class<T> type) {
        T target = null;
        try {
            target = getObjectMapper().readValue(json, type);
        } catch (Exception e) {
            log.warn("logging object error, obj: [{}]", e.getMessage());
        }

        return target;
    }

    public static <T> T jsonToObject(String json, TypeReference<T> type) {
        T target = null;
        try {
            target = getObjectMapper().readValue(json, type);
        } catch (Exception e) {
            log.warn("logging object error, obj: [{}]", e.getMessage());
        }
        return target;
    }

    public static String readFileAsString(String pathLocation) throws Exception {
        Path dataPath = Paths.get(ClassLoader.getSystemResource(pathLocation).toURI());
        return new String(Files.readAllBytes(dataPath));
    }

    public static String getStringFromJsonFile(String jsonFile) {
        ClassPathResource resource = new ClassPathResource(jsonFile);
        try {
            File resourceFile = resource.getFile();
            return new String(Files.readAllBytes(resourceFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error in getObjectFromJsonFile : " + e.getMessage(), e);
//            throw new Exception(e);
        }

        return null;
    }
}
