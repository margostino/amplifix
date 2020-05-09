package org.gaussian.amplifix.toolkit.model;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import org.gaussian.amplifix.toolkit.annotation.Trace;

import java.time.Instant;

/**
 * This class is required to publish in event bus a conversion event to evaluate.
 * If this event is approved according the field configured, the drop metric will be dismissed.
 */
@Trace
@AllArgsConstructor
public class TracedEvent {

    public Instant timestamp;
    public JsonObject event;

}
