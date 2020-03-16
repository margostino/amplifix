package toolkit.metadatareader;

import toolkit.annotation.ConversionRegister;
import toolkit.annotation.Counter;
import toolkit.annotation.Drop;
import toolkit.annotation.DropRegister;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class MetadataReader<E> {

    public List<MetricAnnotation> read(E event) {
        List<MetricAnnotation> metricMetadata = new ArrayList<>();
        List<Annotation> annotations = asList(event.getClass().getAnnotations());

        for (Annotation annotation : annotations) {
            if (annotation instanceof Drop) {
                metricMetadata.add(DropAnnotation.of(annotation));
            }
            if (annotation instanceof Counter) {
                metricMetadata.add(CounterAnnotation.of(annotation));
            }
            if (annotation instanceof DropRegister) {
                metricMetadata.add(DropRegisterAnnotation.of(annotation));
            }
            if (annotation instanceof ConversionRegister) {
                metricMetadata.add(ConversionRegisterAnnotation.of(annotation));
            }
        }

        return metricMetadata;
    }
}
