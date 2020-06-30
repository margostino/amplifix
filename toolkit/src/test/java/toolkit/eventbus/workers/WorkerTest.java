package toolkit.eventbus.workers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.worker.MetricWorker;
import org.gaussian.amplifix.toolkit.worker.TraceWorker;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.processor.CounterProcessor;
import org.gaussian.amplifix.toolkit.processor.TraceProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static io.micrometer.core.instrument.Meter.Type.COUNTER;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkerTest {

    private JsonObject data;
    private Event event;

    @Before
    public void setup() {
        data = new JsonObject().put("one.event", "test");
        event = new Event(new EventMetadata(now(), new ArrayList()), data);
    }

    @Test
    public void metric() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        Counter counter = mock(Counter.class);
        Meter.Id id = mock(Meter.Id.class);
        doNothing().when(counter).increment();
        when(id.getType()).thenReturn(COUNTER);
        when(counter.getId()).thenReturn(id);
        when(counterProcessor.process(any(Event.class))).thenReturn(counter);

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor));
        metricWorker.execute(event);

        verify(counter, times(1)).increment();
    }

    @Test
    public void trace() {

        TraceProcessor traceProcessor = mock(TraceProcessor.class);
        when(traceProcessor.process(any(Event.class))).thenReturn("logging.mock");

        TraceWorker traceWorker = new TraceWorker(asList(traceProcessor));
        traceWorker.execute(event);

        verify(traceProcessor, times(1)).process(any(Event.class));
    }
}
