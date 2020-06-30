package org.gaussian.amplifix.toolkit.processor;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.annotation.Trace;
import org.gaussian.amplifix.toolkit.model.Event;

import static org.gaussian.amplifix.toolkit.json.JsonCodec.encode;


@Slf4j
public class TraceProcessor extends EventProcessor {

    public TraceProcessor() {
        super(Trace.class);
    }

    protected String getData(Event event) {
        JsonObject data = event.data;
        JsonArray tags = new JsonArray(encode(event.metadata.tags));
        //new  String(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(event.data.encode().getBytes())))
        return serialize(data, tags);
    }

    private String serialize(JsonObject data, JsonArray tags) {
        return new JsonObject()
                .put("data", data)
                .put("tags", tags)
                .encode();

    }

}
