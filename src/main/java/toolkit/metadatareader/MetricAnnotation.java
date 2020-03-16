package toolkit.metadatareader;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.lang.annotation.Annotation;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "id", visible = true)
@JsonSubTypes({
               @JsonSubTypes.Type(value = CounterAnnotation.class, name = "counter"),
               @JsonSubTypes.Type(value = DropAnnotation.class, name = "drop"),
               @JsonSubTypes.Type(value = ConversionRegisterAnnotation.class, name = "conversion_register"),
               @JsonSubTypes.Type(value = DropRegisterAnnotation.class, name = "drop_register")
              })
public interface MetricAnnotation {

    boolean filter(Class<? extends Annotation> annotation);

}
