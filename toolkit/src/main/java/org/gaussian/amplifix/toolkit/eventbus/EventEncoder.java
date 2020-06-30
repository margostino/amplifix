package org.gaussian.amplifix.toolkit.eventbus;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import org.gaussian.amplifix.toolkit.metadatareader.MetadataReader;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.model.EventTag;

import java.util.List;

import static io.vertx.core.json.JsonObject.mapFrom;
import static java.time.Instant.now;
import static java.util.Collections.emptyList;

@AllArgsConstructor
public class EventEncoder<E> {

    private final MetadataReader metadataReader;

    public EventEncoder() {
        this.metadataReader = new MetadataReader();
    }

    public JsonObject encode(E event) {
        JsonObject data = mapFrom(event);
        EventMetadata metadata = new EventMetadata(now(), metadataReader.read(event));
        return mapFrom(new Event(metadata, data));
    }

    public JsonObject encode(E event, List<EventTag> tags) {
        JsonObject data = mapFrom(event);
        EventMetadata metadata = new EventMetadata(now(), metadataReader.read(event), tags);
        return mapFrom(new Event(metadata, data));
    }

}
