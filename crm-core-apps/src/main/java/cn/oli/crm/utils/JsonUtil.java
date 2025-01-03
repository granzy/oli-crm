package cn.oli.crm.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author csj
 */
@Slf4j
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 支持Java8之后的time api
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        MAPPER.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
    }

    public static String object2Json(Object o) {
        if (o == null) {
            return null;
        }
        String s = null;
        try {
            s = MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            log.error("object2Json error", e);
        }
        return s;
    }

    public static <T> List<String> listObject2ListJson(List<T> objects) {
        if (objects == null) {
            return null;
        }
        List<String> lists = new ArrayList<>();
        for (T t : objects) {
            lists.add(JsonUtil.object2Json(t));
        }
        return lists;
    }

    public static <T> List<T> listJson2ListObject(List<String> jsons, Class<T> c) {
        if (jsons == null) {
            return null;
        }
        List<T> ts = new ArrayList<>();
        for (String j : jsons) {
            ts.add(JsonUtil.json2Object(j, c));
        }
        return ts;
    }

    public static <T> T json2Object(JsonNode jsonNode, TypeReference<T> tr) {
        String json = object2Json(jsonNode);
        return json2Object(json, tr);
    }


    public static <T> T json2Object(String json, Class<T> c) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }
        T t = null;
        try {
            t = MAPPER.readValue(json, c);
        } catch (Exception e) {
            log.error("json2Object error", e);
        }
        return t;
    }

    public static <T> T json2Object(String json, TypeReference<T> tr) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }
        T t = null;
        try {
            t = MAPPER.readValue(json, tr);
        } catch (Exception e) {
            log.error("json2Object error", e);
        }
        return t;
    }

    public static <T> T object2Map(Object object, Class<T> c) {
        T t = null;
        try {
            t = MAPPER.readValue(MAPPER.writeValueAsString(object), c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static ObjectNode newObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static JsonNode newArrayNode() {
        return MAPPER.createArrayNode();
    }
}
