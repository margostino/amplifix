package toolkit.eventbus;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import toolkit.metadatareader.MetadataReader;

import static io.vertx.core.json.JsonObject.mapFrom;

public class AmplifixEventBus<E> {

    private final EventBus eventBus;
    private final MetadataReader metadataReader;
    private final String DEFAULT_ADDRESS = "amplifix.events";

    public AmplifixEventBus(EventBus eventBus, MetadataReader metadataReader, EventConsumer eventConsumer) {
        this.eventBus = eventBus;
        this.metadataReader = metadataReader;
        this.eventBus.consumer(DEFAULT_ADDRESS, eventConsumer::handle);
    }

    public AmplifixEventBus send(E event) {
        eventBus.send(DEFAULT_ADDRESS, encode(event));
        return this;
    }

    private JsonObject encode(E event) {

        JsonObject data;
        EventMetadata metadata;
        JsonObject control;

        if (event instanceof ConversionEvent) {
            ConversionEvent conversionEvent = (ConversionEvent) event;
            data = conversionEvent.event;
            control = new JsonObject().put("key", conversionEvent.key)
                                      .put("control", conversionEvent.control);
        } else {
            data = mapFrom(event);
            control = new JsonObject();
        }

        metadata = new EventMetadata(metadataReader.read(event), control);
        return mapFrom(new Event(metadata, data));

    }


}
