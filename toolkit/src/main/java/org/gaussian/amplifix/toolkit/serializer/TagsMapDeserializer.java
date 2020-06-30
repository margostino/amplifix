package org.gaussian.amplifix.toolkit.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.gaussian.amplifix.toolkit.model.EventTag;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class TagsMapDeserializer { //extends JsonDeserializer {

    //@Override
    public List<EventTag> deserialize(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return asList(new EventTag(key, key));
    }
}