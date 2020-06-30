package org.gaussian.amplifix.toolkit.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.StringWriter;

public class MyPairSerializer { //extends JsonSerializer {

    private ObjectMapper mapper = new ObjectMapper();

    //@Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers)  throws IOException, JsonProcessingException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, "ds");
        gen.writeFieldName(writer.toString());
    }
}
