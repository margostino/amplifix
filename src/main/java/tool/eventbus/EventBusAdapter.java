package tool.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import tool.metric.CounterRegistry;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class EventBusAdapter<T> {

    public EventBus eventBus;
    public EventListener listener;

    public EventBusAdapter(CounterRegistry counterRegistry) {
        this.eventBus = new AsyncEventBus(newFixedThreadPool(10));
        this.listener = new EventListener(counterRegistry);
        this.eventBus.register(listener);
    }

    public void post(T event) {
//        String eventString;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            SimpleModule module = new SimpleModule("CustomCarSerializer", new Version(1, 0, 0, null, null, null));
//            module.addSerializer(Event.class, new EventSerializer());
//            mapper.registerModule(module);
//            Event event = new Event(request);
//            eventString = mapper.writeValueAsString(event);
//            eventBus.post(eventString);
//        } catch (JsonProcessingException e) {
//            System.out.println(e.getMessage());
//        }


        eventBus.post(event);
    }

}
