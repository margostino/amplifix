package org.gaussian.amplifix.toolkit.metadatareader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;
import org.gaussian.amplifix.toolkit.annotation.Trace;

import java.lang.annotation.Annotation;

@JsonTypeName("trace")
public class TraceAnnotation implements MetricAnnotation {

    @JsonIgnore
    @JsonProperty("id")
    public final String id;

    @JsonCreator
    private TraceAnnotation(@JsonProperty("id") String id) {
        this.id = id;
    }

    public static TraceAnnotation of(Annotation annotation) {
        Trace metadata = (Trace) annotation;
        return new TraceAnnotation(metadata.id());
    }

    public boolean filter(Class<? extends Annotation> annotationClass) {
        return annotationClass == Trace.class;
    }
}
