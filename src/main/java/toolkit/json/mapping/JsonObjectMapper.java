package toolkit.json.mapping;

import io.vertx.core.json.JsonObject;

/**
 * Object <-> JsonObject conversion
 */
public interface JsonObjectMapper {

    /**
     * Convenience factory method to get a default implementation.
     *
     * @return a new instance of the default implementation
     */
    static JsonObjectMapper create() {
        return new JacksonJsonMapper();
    }

    /**
     * Creates a new instance of the specified class from a given JsonObject.
     *
     * @param json  JsonObject to be read
     * @param clazz to instance
     * @param <T>   to define return type
     * @return an instance of the given class, loaded with data read from JsonObject
     */
    <T> T from(JsonObject json, Class<T> clazz);

    /**
     * Create a JsonObject from the fields of a Java object.
     *
     * @param obj to be mapped
     * @return JsonObject mapped from the Java object
     */
    JsonObject json(Object obj);

}
