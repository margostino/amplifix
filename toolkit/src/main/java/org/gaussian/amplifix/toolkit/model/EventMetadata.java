package org.gaussian.amplifix.toolkit.model;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
public class EventMetadata {

    public final Instant timestamp;
    public final List<MetricAnnotation> annotations;

}
