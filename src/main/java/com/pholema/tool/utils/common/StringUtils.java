package com.pholema.tool.utils.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;

public class StringUtils {

    private static Logger logger = Logger.getLogger(StringUtils.class);

    private static ObjectMapper jsonMapper = new ObjectMapper();
    private static Gson gson = new Gson();
    private static JsonParser jsonParser = new JsonParser();

    static {
        jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /*
    json method
     */

    public static <T> T readJSON(String jsonString, Class<T> clazz) {
        if (jsonString != null) {
            try {
                return jsonMapper.readValue(jsonString, clazz);
            } catch (Exception e) {
                logger.error("jsonMapper error:" + e.getMessage());
            }
        }
        return null;
    }

    public static <T> List<T> readJSON(String jsonString, TypeReference<List<T>> typeReference) {
        if (jsonString != null) {
            try {
                return jsonMapper.readValue(jsonString, typeReference);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static <T> T readJSON(Object object, Class<T> clazz) {
        return jsonMapper.convertValue(object, clazz);
    }

    /*
     * object to Json string
     *
     */
    public static String writeJSON(Object object) {
        if (object == null) {
            return null; // else JSON_MAPPER.writeValueAsString(object) will return "null"
        }
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("jsonMapper error:" + e.getMessage());
        }
        return null;
    }

    /*
    gson method
     */

    public static String toGson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromGson(String str, Type type) {
        return gson.fromJson(str, type);
    }

    public static JsonObject fromGson(String json) {
        return jsonParser.parse(json).getAsJsonObject();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String null2Empty(String str) {
        return isEmpty(str) ? "" : str;
    }
}
