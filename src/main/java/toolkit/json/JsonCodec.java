package toolkit.json;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.codec.BodyCodec;
import toolkit.json.mapping.JsonObjectMapper;
import toolkit.json.mapping.JsonStringCodec;

import java.util.function.Function;


/**
 * Utility for JSON handling, using global standard mapping settings.
 */
public final class JsonCodec {

    private static final JsonStringCodec codec = JsonStringCodec.create();
    private static final JsonObjectMapper mapper = JsonObjectMapper.create();

    private JsonCodec() {
    }

    public static <T> T decode(String json, Class<T> clazz) {
        return codec.decode(json, clazz);
    }

    public static String encode(Object object) {
        return codec.encode(object);
    }

    public static <T> T from(JsonObject json, Class<T> clazz) {
        return mapper.from(json, clazz);
    }

    public static JsonObject json(Object object) {
        return mapper.json(object);
    }

    /**
     * Convenient method to create a BodyCodec to load JSON data into a new instance of the given class.
     *
     * @param clazz to instance
     * @param <T>   to define type of BodyCodec
     * @return a BodyCoded to read JSON data into instances of the given class
     */
    public static <T> BodyCodec<T> as(Class<T> clazz) {
        final Function<Buffer, T> reader = buffer -> decode(buffer.toString(), clazz);
        return BodyCodec.create(reader);
    }

}