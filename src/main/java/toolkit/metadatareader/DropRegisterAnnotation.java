package toolkit.metadatareader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import toolkit.annotation.DropRegister;

import java.lang.annotation.Annotation;

@JsonTypeName("drop_register")
public class DropRegisterAnnotation implements MetricAnnotation {

    @JsonIgnore
    @JsonProperty("id")
    public final String id;

    @JsonCreator
    private DropRegisterAnnotation(@JsonProperty("id") String id) {
        this.id = id;
    }

    public static DropRegisterAnnotation of(Annotation annotation) {
        DropRegister metadata = (DropRegister) annotation;
        return new DropRegisterAnnotation(metadata.id());
    }

    public boolean filter(Class<? extends Annotation> annotationClass) {
        return annotationClass == DropRegister.class;
    }
}
