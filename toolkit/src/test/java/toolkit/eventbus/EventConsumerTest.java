package toolkit.eventbus;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.eventbus.EventConsumer;
import org.gaussian.amplifix.toolkit.metric.AsyncWorker;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.model.EventMetadata;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static io.vertx.core.json.JsonObject.mapFrom;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class EventConsumerTest {

    @Mock
    AsyncWorker worker = mock(AsyncWorker.class);
    @Mock
    Message message = mock(Message.class);

    @Test
    public void startup() {
        JsonObject data = new JsonObject().put("startup", "test");
        Event event = new Event(new EventMetadata(now(), new ArrayList()), data);
        when(message.body()).thenReturn(mapFrom(event));

        EventConsumer consumer = new EventConsumer(asList(worker));
        consumer.handle(message);

        verify(worker, times(0)).execute(event);
    }

    @Test
    public void event() {
        JsonObject data = new JsonObject().put("one.event", "test");
        Event event = new Event(new EventMetadata(now(), new ArrayList()), data);

        doNothing().when(worker).execute(event);
        when(message.body()).thenReturn(mapFrom(event));

        EventConsumer consumer = new EventConsumer(asList(worker));
        consumer.handle(message);

        verify(worker, times(1)).execute(any(Event.class));

    }

}
