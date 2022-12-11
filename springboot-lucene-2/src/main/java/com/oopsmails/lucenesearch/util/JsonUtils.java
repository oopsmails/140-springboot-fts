package com.oopsmails.lucenesearch.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonUtils {
//    public static String PROJECT_PATH = "C:\\github\\springboot-dev-repo-develop\\spring-boot-mock-backend\\src\\main\\resources";

//    public static String getFileNameWithPath(String fileName, String packageString) {
//        return PROJECT_PATH + (packageString == null ? "" : packageString) + "/" + fileName;
//    }

    public static ObjectMapper getObjectMapper() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static void processObject(Object result) throws Exception {
        System.out.println("result = " + result);

        String jsonStr = getObjectMapper().writeValueAsString(result);
        System.out.println("jsonStr = " + jsonStr);

        String prettyJsonStr = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println("prettyJsonStr = " + prettyJsonStr);
    }

    public static void logObject(Object obj) {
        String prettyJsonStr = null;
        try {
            prettyJsonStr = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            System.out.println("<Albert> logging object error, obj: " + obj);
            System.out.println("<Albert> logging object error, exception will be ignored: " + e);
        }
        System.out.println("<Albert> logging object: " + prettyJsonStr);
    }

    public static void printJsonObject(Object obj) {
        printJsonObject("", obj);
    }

    public static void printJsonObject(String addingMessage, Object obj) {
        String added = addingMessage == null ? "" : addingMessage;
        String prettyJsonStr = null;
        try {
            prettyJsonStr = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            System.out.println("<Albert> logging object error, obj: " + obj);
            System.out.println("<Albert> logging object error, exception will be ignored: " + e);
        }
        System.out.println(added + "- <Albert> logging object: " + prettyJsonStr);
    }

    public static <T> T jsonFileToObject(String fileNameWithPath, Class<T> type) {
        T target = null;
        try {
            File jsonFile = new File(fileNameWithPath);
            target = getObjectMapper().readValue(jsonFile, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    public static <T> T jsonFileToObject(String fileNameWithPath, TypeReference<T> type) {
        T target = null;
        try {
            File jsonFile = new File(fileNameWithPath);
            target = getObjectMapper().readValue(jsonFile, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    public static <T> T jsonToObject(String json, Class<T> type) {
        T target = null;
        try {
            target = getObjectMapper().readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    public static <T> T jsonToObject(String json, TypeReference<T> type) {
        T target = null;
        try {
            target = getObjectMapper().readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public static String readFileAsString(String pathLocation) throws Exception {
        Path dataPath = Paths.get(ClassLoader.getSystemResource(pathLocation).toURI());
        return new String(Files.readAllBytes(dataPath));
    }
}
