package toolkit.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import toolkit.metric.Metric;
import toolkit.metric.MetricBuilder;
import toolkit.metric.MetricSender;

import java.util.List;

@Slf4j
public class EventListener<T> {

    private MetricBuilder metricBuilder;
    private MetricSender metricSender;

    public EventListener(MetricSender metricSender, MetricBuilder metricBuilder) {
        this.metricBuilder = metricBuilder;
        this.metricSender = metricSender;
    }

    @Subscribe
    public void listen(T event) {
        final List<Metric> metrics = metricBuilder.build(event);
        metricSender.send(metrics);
    }

}
