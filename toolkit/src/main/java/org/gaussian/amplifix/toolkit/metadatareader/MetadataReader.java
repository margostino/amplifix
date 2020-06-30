package org.gaussian.amplifix.toolkit.metadatareader;

import org.gaussian.amplifix.toolkit.annotation.ConversionRegister;
import org.gaussian.amplifix.toolkit.annotation.Counter;
import org.gaussian.amplifix.toolkit.annotation.Drop;
import org.gaussian.amplifix.toolkit.annotation.DropRegister;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;
import org.gaussian.amplifix.toolkit.annotation.Trace;

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
            if (annotation instanceof Trace) {
                metricMetadata.add(TraceAnnotation.of(annotation));
            }
        }

        return metricMetadata;
    }
}
