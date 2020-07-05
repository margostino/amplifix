package org.gaussian.amplifix.toolkit.eventbus;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class AmplifixEventBus<E> {

    private final EventBus vertxEventBus;
    private static String DEFAULT_ADDRESS = "amplifix.events";

    public AmplifixEventBus(EventBus vertxEventBus) {
        this.vertxEventBus = vertxEventBus;
    }

    public void setConsumer(EventConsumer consumer) {
        this.vertxEventBus.consumer(DEFAULT_ADDRESS, consumer::handle);
    }

    public AmplifixEventBus send(JsonObject event) {
        vertxEventBus.send(DEFAULT_ADDRESS, event);
        return this;
    }

}
