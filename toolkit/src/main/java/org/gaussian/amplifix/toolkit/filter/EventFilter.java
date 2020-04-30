package org.gaussian.amplifix.toolkit.filter;

import lombok.AllArgsConstructor;
import org.gaussian.amplifix.toolkit.metadatareader.MetricAnnotation;

import java.lang.annotation.Annotation;

@AllArgsConstructor
public class EventFilter<E, A extends Annotation> {

    protected Class<A> annotationClass;

    public boolean filter(MetricAnnotation metricAnnotation) {
        return false;//annotation.annotationType() == annotationClass;
    }

    public A getAnnotation(E event) {
        return event.getClass().getAnnotation(annotationClass);
    }

}
