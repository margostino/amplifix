package toolkit.processor;

import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.annotation.Counter;
import org.gaussian.amplifix.toolkit.annotation.Trace;
import org.gaussian.amplifix.toolkit.metadatareader.TraceAnnotation;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.metadatareader.CounterAnnotation;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;

import static java.util.Arrays.asList;
import static toolkit.processor.AnnotationFactory.counter;
import static java.time.Instant.now;
import static toolkit.processor.AnnotationFactory.trace;


public class EventFactory {

    public static Event eventWithCounter(JsonObject data, String[] fields) {
        Counter counter = counter(fields);
        MetricAnnotation metricAnnotation = CounterAnnotation.of(counter);
        EventMetadata metadata = new EventMetadata(now(), asList(metricAnnotation));
        return new Event(metadata, data);
    }

    public static Event eventWithTrace(JsonObject data) {
        Trace trace = trace();
        MetricAnnotation metricAnnotation = TraceAnnotation.of(trace);
        EventMetadata metadata = new EventMetadata(now(), asList(metricAnnotation));
        return new Event(metadata, data);
    }
}
