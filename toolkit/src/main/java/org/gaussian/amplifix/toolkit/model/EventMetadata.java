package org.gaussian.amplifix.toolkit.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;

import java.time.Instant;
import java.util.List;

import static java.util.Collections.emptyList;

public class EventMetadata {

    public final Instant timestamp;
    public final List<MetricAnnotation> annotations;
    public final List<EventTag> tags;

    @JsonCreator
    public EventMetadata(@JsonProperty("timestamp") Instant timestamp,
                         @JsonProperty("annotations") List<MetricAnnotation> annotations,
                         @JsonProperty("tags") List<EventTag> tags) {
        this.timestamp = timestamp;
        this.annotations = annotations;
        this.tags = tags;
    }

    public EventMetadata(@JsonProperty("timestamp") Instant timestamp,
                         @JsonProperty("annotations") List<MetricAnnotation> annotations) {
        this.timestamp = timestamp;
        this.annotations = annotations;
        this.tags = emptyList();
    }

}
