package toolkit.metadatareader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import toolkit.annotation.Counter;

import java.lang.annotation.Annotation;
import java.util.List;

import static java.util.Arrays.asList;

@JsonTypeName("counter")
public class CounterAnnotation implements MetricAnnotation {

    @JsonIgnore
    @JsonProperty("id")
    public final String id;
    @JsonProperty("metric_name")
    public final String metricName;
    public final List<String> fields;

    @JsonCreator
    private CounterAnnotation(@JsonProperty("id") String id,
                              @JsonProperty("metric_name") String metricName,
                              @JsonProperty("fields") List<String> fields) {
        this.id = id;
        this.metricName = metricName;
        this.fields = fields;
    }

    public static CounterAnnotation of(Annotation annotation) {
        Counter metadata = (Counter) annotation;
        return new CounterAnnotation(metadata.id(), metadata.metricName(), asList(metadata.fields()));
    }

    public boolean filter(Class<? extends Annotation> annotationClass) {
        return annotationClass == Counter.class;
    }
}
