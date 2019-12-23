package toolkit.eventbus;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import toolkit.configuration.MetricConfiguration;
import toolkit.metric.CounterRegistry;
import toolkit.metric.Metric;
import toolkit.metric.MetricBuilder;
import toolkit.metric.MetricSender;

import java.util.List;

@Slf4j
public class EventListener<T> {

    private Gson gson;
    private MetricBuilder metricBuilder;
    private MetricSender metricSender;
    public MetricConfiguration metricConfiguration;

    public EventListener(CounterRegistry counterRegistry, MetricBuilder metricBuilder) {
        this.gson = new Gson();
        this.metricBuilder = metricBuilder;
        this.metricSender = new MetricSender(counterRegistry);
    }

    public EventListener(CounterRegistry counterRegistry) {
        this.gson = new Gson();
        this.metricBuilder = new MetricBuilder("amplifix");
        this.metricSender = new MetricSender(counterRegistry);
    }

    @Subscribe
    public void listen(T event) {
        //final String json = gson.toJson(event);
        final List<Metric> metrics = metricBuilder.build(event);
        metricSender.send(metrics);
    }

    private Object getValue(T event, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return event.getClass()
                    .getField(fieldName)
                    .get(event);
    }

    private Class<?> getType(T event, String fieldName) throws NoSuchFieldException {
        return event.getClass()
                    .getField(fieldName)
                    .getType();
    }
}
