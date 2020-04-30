package org.gaussian.amplifix.toolkit.eventbus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.metadatareader.MetricAnnotation;

import java.util.List;

public class EventMetadata {

    public final List<MetricAnnotation> annotations;
    public final JsonObject control;

    @JsonCreator
    public EventMetadata(@JsonProperty("annotations") List<MetricAnnotation> annotations,
                         @JsonProperty("control") JsonObject control) {
        this.annotations = annotations;
        this.control = control;
    }

}
