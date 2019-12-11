package tool.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonSerializer implements Serializer {

    private final ObjectReader reader;
    private final ObjectWriter writer;

    public JsonSerializer(@Qualifier("object_mapper") ObjectMapper mapper) {
        this.reader = mapper.reader();
        this.writer = mapper.writer();
    }

    @Override
    public <T> String serialize(T object) {
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(String message, Class<T> type) {
        try {
            return reader.forType(type).readValue(message);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage() + System.lineSeparator() + message, e);
        }
    }

    @Override
    public <T> T deserialize(InputStream message, Class<T> type) {
        try {
            return reader.forType(type).readValue(message);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage() + System.lineSeparator() + message, e);
        }
    }

}