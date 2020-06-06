package toolkit.processor;

import org.gaussian.amplifix.toolkit.annotation.Counter;
import org.gaussian.amplifix.toolkit.annotation.Trace;

import java.lang.annotation.Annotation;

public class AnnotationFactory {

    public static Counter counter(String[] fields) {
        return new Counter() {
            @Override
            public String id() {
                return "counter";
            }

            @Override
            public String metricName() {
                return "mock.metric.name";
            }

            @Override
            public String[] fields() {
                return fields;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Counter.class;
            }
        };
    }

    public static Trace trace() {
        return new Trace() {
            @Override
            public String id() {
                return "trace";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Trace.class;
            }
        };
    }
}
