package toolkit.eventbus;

import io.micrometer.core.instrument.Meter;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.datagrid.DropRegistry;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.eventbus.EventConsumer;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.gaussian.amplifix.toolkit.metric.MetricBuilder;
import org.gaussian.amplifix.toolkit.metric.MetricSender;
import org.gaussian.amplifix.toolkit.model.TagSerializable;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Instant;
import java.util.ArrayList;

import static io.micrometer.core.instrument.Metrics.counter;
import static io.vertx.core.json.JsonObject.mapFrom;
import static java.util.Arrays.asList;
import static org.gaussian.amplifix.toolkit.json.JsonCodec.decode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.time.Instant.now;


public class EventConsumerTest {

    @Mock
    MetricSender sender = mock(MetricSender.class);
    @Mock
    MetricBuilder builder = mock(MetricBuilder.class);
    @Mock
    Message message = mock(Message.class);

    @Test
    public void startup() {
        JsonObject data = new JsonObject().put("startup", "test");
        Event event = new Event(new EventMetadata(now(), new ArrayList()), data);
        when(message.body()).thenReturn(mapFrom(event));

        EventConsumer consumer = new EventConsumer(sender, builder);
        consumer.handle(message);

        verify(sender, times(0)).send(anyString());
        verify(builder, times(0)).build(any());
    }

    @Test
    public void event() {
        JsonObject jsonObject = new JsonObject()
                .put("metric_name", "dummy")
                .put("key", "one")
                .put("value", "two")
                .put("tags", asList(new TagSerializable("t1", "t2")))
                .put("event", "three")
                .put("timestamp", Instant.now().toString());

        DropRegistry dropRegistry = decode(jsonObject.encode(), DropRegistry.class);

        JsonObject data = new JsonObject().put("one.event", "test");
        Meter meter = counter("metric.mock", new ArrayList<>());
        Event event = new Event(new EventMetadata(now(), new ArrayList()), data);

        doNothing().when(sender).send(any(Meter.class));
        when(builder.build(any(Event.class))).thenReturn(asList(meter));
        when(message.body()).thenReturn(mapFrom(event));

        EventConsumer consumer = new EventConsumer(sender, builder);
        consumer.handle(message);

        verify(sender, times(1)).send(eq(meter));
        verify(builder, times(1)).build(any(Event.class));

    }
}
