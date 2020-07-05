package toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.vertx.core.json.JsonObject;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.processor.CounterProcessor;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;
import org.gaussian.amplifix.toolkit.processor.TraceProcessor;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static toolkit.factory.EventFactory.eventWithCounter;
import static toolkit.factory.EventFactory.eventWithTrace;

public class EventProcessorTest {

    private JsonObject data;

    @Before
    public void setup() {
        data = new JsonObject().put("session_id", "mock.session.id")
                               .put("description", "mock test rock")
                               .put("created_at", Instant.now().toString())
                               .put("country", "SE")
                               .put("status", "APPROVED")
                               .put("payment_method_categories", asList("pay_later", "pay_now"));
    }

    @Test
    public void counterWithMultiAndSimpleTag() {
        EventProcessor processor = new CounterProcessor();
        Event event = eventWithCounter(data, new String[]{"country", "payment_method_categories"});
        List<Meter> meters = (List) processor.process(event);

        assertEquals(meters.size(), 2);
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getType().equals(Meter.Type.COUNTER)));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getName().equals("amplifix.mock.metric.name")));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().size() == 2));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("country") && tag.getValue().equals("SE"))));
        assertTrue(meters.stream().anyMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("payment_method_categories") && tag.getValue().equals("pay_later"))));
        assertTrue(meters.stream().anyMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("payment_method_categories") && tag.getValue().equals("pay_now"))));
    }

    @Test
    public void counterWithTwoSimpleTag() {
        EventProcessor processor = new CounterProcessor();
        Event event = eventWithCounter(data, new String[]{"country", "status"});
        List<Meter> meters = (List) processor.process(event);

        assertEquals(meters.size(), 1);
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getType().equals(Meter.Type.COUNTER)));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getName().equals("amplifix.mock.metric.name")));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().size() == 2));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("country") && tag.getValue().equals("SE"))));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("status") && tag.getValue().equals("APPROVED"))));
    }

    @Test
    public void counterWithOneSimpleTag() {
        EventProcessor processor = new CounterProcessor();
        Event event = eventWithCounter(data, new String[]{"country"});
        List<Meter> meters = (List) processor.process(event);

        assertEquals(meters.size(), 1);
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getType().equals(Meter.Type.COUNTER)));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getName().equals("amplifix.mock.metric.name")));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().size() == 1));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("country") && tag.getValue().equals("SE"))));
    }

    @Test
    public void counterWithMultiTag() {
        EventProcessor processor = new CounterProcessor();
        Event event = eventWithCounter(data, new String[]{"payment_method_categories"});
        List<Meter> meters = (List) processor.process(event);

        assertEquals(meters.size(), 2);
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getType().equals(Meter.Type.COUNTER)));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getName().equals("amplifix.mock.metric.name")));
        assertTrue(meters.stream().allMatch(meter -> meter.getId().getTags().size() == 1));
        assertTrue(meters.stream().anyMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("payment_method_categories") && tag.getValue().equals("pay_later"))));
        assertTrue(meters.stream().anyMatch(meter -> meter.getId().getTags().stream().anyMatch(tag -> tag.getKey().equals("payment_method_categories") && tag.getValue().equals("pay_now"))));
    }

    @Test
    public void trace() {
        EventProcessor processor = new TraceProcessor();
        Event event = eventWithTrace(data);
        JsonObject trace = new JsonObject((String) processor.process(event));

        assertEquals(trace.getString("session_id"), "mock.session.id");
        assertEquals(trace.getString("description"), "mock test rock");
        assertTrue(trace.getString("created_at") != null);
        assertEquals(trace.getString("country"), "SE");
        assertEquals(trace.getString("status"), "APPROVED");
        assertTrue(trace.getJsonArray("payment_method_categories").contains("pay_later"));
        assertTrue(trace.getJsonArray("payment_method_categories").contains("pay_now"));
    }
}
