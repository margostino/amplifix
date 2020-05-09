package org.gaussian.amplifix.toolkit.eventbus;

import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.model.ConversionEvent;
import org.gaussian.amplifix.toolkit.model.TracedEvent;

import java.lang.reflect.Field;

import static io.vertx.core.json.JsonObject.mapFrom;
import static java.time.Instant.now;

public class AmplifixSender<E> {

    private final AmplifixEventBus eventBus;
    private final EventEncoder encoder;

    public AmplifixSender(AmplifixEventBus eventBus) {
        this.encoder = new EventEncoder();
        this.eventBus = eventBus;
    }

    public void send(E event) {
        eventBus.send(encoder.encode(event));
    }

    public void trace(E event) {
        TracedEvent tracedEvent = new TracedEvent(now(), mapFrom(event));
        eventBus.send(encoder.encode(tracedEvent));
    }

    public void send(E event, String conversionKey) {
        Field[] fields = event.getClass().getFields();
        JsonObject encodedEvent = encoder.encode(event);
        ConversionEvent conversionEvent = ConversionEvent.of(conversionKey, encodedEvent, fields);
        eventBus.send(encoder.encode(conversionEvent));
    }

}
