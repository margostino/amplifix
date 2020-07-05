package toolkit.eventbus.workers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.processor.ConversionProcessor;
import org.gaussian.amplifix.toolkit.processor.CounterProcessor;
import org.gaussian.amplifix.toolkit.processor.DropProcessor;
import org.gaussian.amplifix.toolkit.processor.RegisterProcessor;
import org.gaussian.amplifix.toolkit.processor.TraceProcessor;
import org.gaussian.amplifix.toolkit.worker.MetricWorker;
import org.gaussian.amplifix.toolkit.worker.TraceWorker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

    private Counter mockCounter() {
        Counter counter = mock(Counter.class);
        Meter.Id id = mock(Meter.Id.class);
        doNothing().when(counter).increment();
        when(id.getType()).thenReturn(COUNTER);
        when(counter.getId()).thenReturn(id);
        return counter;
    }

    @Test
    public void oneCounterMetric() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        Counter counter = mockCounter();
        when(counterProcessor.process(any(Event.class))).thenReturn(asList(counter));

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor));
        metricWorker.execute(event);

        verify(counter, times(1)).increment();
    }

    @Test
    public void twoCounterMetrics() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        Counter firstCounter = mockCounter();
        Counter secondCounter = mockCounter();
        when(counterProcessor.process(any(Event.class))).thenReturn(asList(firstCounter, secondCounter));

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor));
        metricWorker.execute(event);

        verify(firstCounter, times(1)).increment();
        verify(secondCounter, times(1)).increment();
    }

    @Test
    public void severalMetricProcessorsButOnlyCounter() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        DropProcessor dropProcessor = mock(DropProcessor.class);
        ConversionProcessor conversionProcessor = mock(ConversionProcessor.class);

        Counter firstCounter = mockCounter();
        Counter secondCounter = mockCounter();

        when(counterProcessor.process(any(Event.class))).thenReturn(asList(firstCounter, secondCounter));
        when(dropProcessor.process(any(Event.class))).thenReturn(emptyList());
        when(conversionProcessor.process(any(Event.class))).thenReturn(emptyList());

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor, dropProcessor, conversionProcessor));
        metricWorker.execute(event);

        verify(firstCounter, times(1)).increment();
        verify(secondCounter, times(1)).increment();
    }

    @Test
    public void severalMetricProcessorsWithCounterAndDrop() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        DropProcessor dropProcessor = mock(DropProcessor.class);
        ConversionProcessor conversionProcessor = mock(ConversionProcessor.class);

        Counter firstCounter = mockCounter();
        Counter secondCounter = mockCounter();
        Counter dropCounter = mockCounter();

        when(counterProcessor.process(any(Event.class))).thenReturn(asList(firstCounter, secondCounter));
        when(dropProcessor.process(any(Event.class))).thenReturn(asList(dropCounter));
        when(conversionProcessor.process(any(Event.class))).thenReturn(emptyList());

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor, dropProcessor, conversionProcessor));
        metricWorker.execute(event);

        verify(firstCounter, times(1)).increment();
        verify(secondCounter, times(1)).increment();
        verify(dropCounter, times(1)).increment();
    }

    @Test
    public void allMetricProcessorsWithCounterAndDrop() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        DropProcessor dropProcessor = mock(DropProcessor.class);
        ConversionProcessor conversionProcessor = mock(ConversionProcessor.class);
        RegisterProcessor registerProcessor = mock(RegisterProcessor.class);

        Counter firstCounter = mockCounter();
        Counter secondCounter = mockCounter();
        Counter dropCounter = mockCounter();

        when(counterProcessor.process(any(Event.class))).thenReturn(asList(firstCounter, secondCounter));
        when(dropProcessor.process(any(Event.class))).thenReturn(asList(dropCounter));
        when(conversionProcessor.process(any(Event.class))).thenReturn(emptyList());
        when(registerProcessor.process(any(Event.class))).thenReturn(emptyList());

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor, dropProcessor, conversionProcessor));
        metricWorker.execute(event);

        verify(firstCounter, times(1)).increment();
        verify(secondCounter, times(1)).increment();
        verify(dropCounter, times(1)).increment();
    }

    @Test
    public void oneProcessorIsFiltered() {

        CounterProcessor counterProcessor = mock(CounterProcessor.class);
        DropProcessor dropProcessor = mock(DropProcessor.class);

        Counter firstCounter = mockCounter();
        Counter secondCounter = mockCounter();

        when(counterProcessor.process(any(Event.class))).thenReturn(asList(firstCounter, secondCounter));
        when(dropProcessor.process(any(Event.class))).thenReturn(null);

        MetricWorker metricWorker = new MetricWorker(asList(counterProcessor, dropProcessor));
        metricWorker.execute(event);

        verify(firstCounter, times(1)).increment();
        verify(secondCounter, times(1)).increment();
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
