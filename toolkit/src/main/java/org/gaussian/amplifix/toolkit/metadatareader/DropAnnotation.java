package org.gaussian.amplifix.toolkit.metadatareader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.gaussian.amplifix.toolkit.annotation.Drop;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

@JsonTypeName("drop")
public class DropAnnotation implements MetricAnnotation {

    @JsonIgnore
    @JsonProperty("id")
    public final String id;
    @JsonProperty("metric_name")
    public final String metricName;
    public final String field;
    public final String event;
    public final long ttl;
    @JsonProperty("time_unit")
    public final TimeUnit timeUnit;
    public final List<String> tags;

    @JsonCreator
    private DropAnnotation(@JsonProperty("id") String id,
                           @JsonProperty("metric_name") String metricName,
                           @JsonProperty("field") String field,
                           @JsonProperty("event") String event,
                           @JsonProperty("ttl") long ttl,
                           @JsonProperty("time_unit") TimeUnit timeUnit,
                           @JsonProperty("tags") List<String> tags) {
        this.id = id;
        this.metricName = metricName;
        this.field = field;
        this.event = event;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
        this.tags = tags;
    }

    public static DropAnnotation of(Annotation annotation) {
        Drop metadata = (Drop) annotation;
        return new DropAnnotation(metadata.id(),
                                  metadata.metricName(),
                                  metadata.field(),
                                  metadata.event(),
                                  metadata.ttl(),
                                  metadata.timeUnit(),
                                  asList(metadata.tags()));
    }

    public boolean filter(Class<? extends Annotation> annotationClass) {
        return annotationClass == Drop.class;
    }
}
