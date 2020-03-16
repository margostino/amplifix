package toolkit.json.mapping;

/**
 * Object <-> String conversion
 */
public interface JsonStringCodec {

    /**
     * Convenience factory method to get a default implementation.
     *
     * @return a new instance of the default implementation
     */
    static JsonStringCodec create() {
        return new JacksonJsonMapper();
    }

    /**
     * Creates a new instance of the specified class loaded from JSON string.
     *
     * @param json  as string to be read
     * @param clazz to instance
     * @param <T>   to define return type
     * @return an instance of the given class, loaded with data read from JSON string
     */
    <T> T decode(String json, Class<T> clazz);

    /**
     * Serializes a Java object to JSON string.
     *
     * @param obj to be encoded
     * @return JSON encoded data as string
     */
    String encode(Object obj);

}
