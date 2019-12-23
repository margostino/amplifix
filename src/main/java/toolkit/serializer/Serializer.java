package toolkit.serializer;

import java.io.InputStream;

public interface Serializer {
    <T> String serialize(T object);

    <T> T deserialize(String value, Class<T> type);

    <T> T deserialize(InputStream value, Class<T> type);
}