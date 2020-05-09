package toolkit.processor;

import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.annotation.Counter;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.metadatareader.CounterAnnotation;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;

import java.time.Instant;

import static java.util.Arrays.asList;
import static toolkit.processor.AnnotationFactory.counter;
import static java.time.Instant.now;


public class EventFactory {

    public static Event eventWithCounter(JsonObject data, String[] fields) {
        Counter counter = counter(fields);
        MetricAnnotation metricAnnotation = CounterAnnotation.of(counter);
        EventMetadata metadata = new EventMetadata(now(), asList(metricAnnotation));
        return new Event(metadata, data);
    }
}
