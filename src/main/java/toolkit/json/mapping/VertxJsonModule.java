package toolkit.json.mapping;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * Jackson module to provide Vert.x's JsonObject and JsonArray support.
 */
public class VertxJsonModule extends SimpleModule {

    public VertxJsonModule() {
        super("vertx-json-module");
        addSerializer(JsonObject.class, new JsonObjectSerializer());
        addSerializer(JsonArray.class, new JsonArraySerializer());
        addDeserializer(JsonObject.class, new JsonObjectDeserializer());
        addDeserializer(JsonArray.class, new JsonArrayDeserializer());
    }

    private static class JsonObjectSerializer extends StdSerializer<JsonObject> {

        JsonObjectSerializer() {
            super(JsonObject.class);
        }

        @Override
        public void serialize(JsonObject value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            generator.writeRawValue(value.encode());
        }

    }

    private static class JsonObjectDeserializer extends StdDeserializer<JsonObject> {

        JsonObjectDeserializer() {
            super(JsonObject.class);
        }

        @Override
        public JsonObject deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return new JsonObject(parser.readValueAsTree().toString());
        }

    }

    private static class JsonArraySerializer extends StdSerializer<JsonArray> {

        JsonArraySerializer() {
            super(JsonArray.class);
        }

        @Override
        public void serialize(JsonArray value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            generator.writeRawValue(value.encode());
        }

    }

    private static class JsonArrayDeserializer extends StdDeserializer<JsonArray> {

        JsonArrayDeserializer() {
            super(JsonArray.class);
        }

        @Override
        public JsonArray deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return new JsonArray(parser.readValueAsTree().toString());
        }

    }

}
