package org.gaussian.amplifix.toolkit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;

import java.util.List;

@AllArgsConstructor
public class Event {

    public final EventMetadata metadata;
    public final JsonObject data;

    @JsonIgnore
    public List<MetricAnnotation> getAnnotations() {
        return metadata.annotations;
    }
}
