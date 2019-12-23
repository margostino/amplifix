package toolkit.eventbus;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class EventSerializer extends StdSerializer<Event> {

    public EventSerializer() {
        this(null);
    }

    public EventSerializer(Class<Event> eventClass) {
        super(eventClass);
    }

    @Override
    public void serialize(Event event, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("country", event.raw);
        jsonGenerator.writeEndObject();
    }

}
