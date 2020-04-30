package org.gaussian.amplifix.toolkit.json.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.JsonObject;

import java.util.Map;

import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

/**
 * Basic implementation to provide JSON mapping using Jackson library.
 * Allows users to provide / customize an ObjectMapper without affecting global, application level settings.
 */
public class JacksonJsonMapper implements JsonObjectMapper, JsonStringCodec {

    private final ObjectMapper objectMapper;

    /**
     * Creates a JsonMapper using a standard object mapper.
     */
    public JacksonJsonMapper() {
        this(createStandardObjectMapper());
    }

    /**
     * Creates a JsonMapper using a custom object mapper.
     *
     * @param objectMapper to be used in JSON handling methods
     */
    public JacksonJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Builds a new object mapper with standard configuration.
     */
    private static ObjectMapper createStandardObjectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
                .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
                .registerModule(new JavaTimeModule())
                //.registerModule(new Jdk8Module())
                .registerModule(new VertxJsonModule());
    }

    @Override
    public <T> T decode(String json, Class<T> clazz) {
        requireNonNull(json, "JSON string required to decode");
        requireNonNull(clazz, "destination class required to decode JSON string");
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            final String message = format("failed to decode JSON string as {0}", clazz.getSimpleName());
            throw new DecodeException(message, e);
        }
    }

    @Override
    public String encode(Object object) {
        requireNonNull(object, "object required to encode as JSON string");
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            final String message =
                    format("failed to encode {0} as JSON string: {1}",
                           object.getClass().getSimpleName(),
                           e.getMessage());
            throw new EncodeException(message);
        }
    }

    @Override
    public <T> T from(JsonObject json, Class<T> clazz) {
        requireNonNull(json, "JsonObject required to load from");
        requireNonNull(clazz, "destination class required to load from JsonObject");
        try {
            return objectMapper.convertValue(json.getMap(), clazz);
        } catch (Exception e) {
            final String message = format("failed to load {0} from JsonObject", clazz.getSimpleName());
            throw new DecodeException(message, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject json(Object object) {
        requireNonNull(object, "object required to map to JsonObject");
        try {
            return new JsonObject((Map<String, Object>) objectMapper.convertValue(object, Map.class));
        } catch (Exception e) {
            final String message =
                    format("failed to map {0} to JsonObject: {1}",
                           object.getClass().getSimpleName(),
                           e.getMessage());
            throw new EncodeException(message);
        }
    }

}