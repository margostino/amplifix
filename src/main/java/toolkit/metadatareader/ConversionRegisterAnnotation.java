package toolkit.metadatareader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import toolkit.annotation.ConversionRegister;

import java.lang.annotation.Annotation;

@JsonTypeName("conversion_register")
public class ConversionRegisterAnnotation implements MetricAnnotation {

    @JsonIgnore
    @JsonProperty("id")
    public final String id;

    @JsonCreator
    private ConversionRegisterAnnotation(@JsonProperty("id") String id) {
        this.id = id;
    }

    public static ConversionRegisterAnnotation of(Annotation annotation) {
        ConversionRegister metadata = (ConversionRegister) annotation;
        return new ConversionRegisterAnnotation(metadata.id());
    }

    public boolean filter(Class<? extends Annotation> annotationClass) {
        return annotationClass == ConversionRegister.class;
    }
}
