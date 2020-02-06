package toolkit.eventfilter;

import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;

@AllArgsConstructor
public class EventFilter<E, A extends Annotation> {

    protected Class<A> annotationClass;

    public boolean filter(Annotation annotation) {
        return annotation.annotationType() == annotationClass;
    }

    public A getAnnotation(E event) {
        return event.getClass().getAnnotation(annotationClass);
    }

}
