package toolkit.eventbus;

import com.google.common.eventbus.Subscribe;
import io.micrometer.core.instrument.Meter;
import lombok.extern.slf4j.Slf4j;
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
        List<Meter> meters = metricBuilder.build(event);
        meters.stream().forEach(metricSender::send);
    }

}
