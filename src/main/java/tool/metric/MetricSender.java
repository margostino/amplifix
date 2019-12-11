package tool.metric;

import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

@Component
public class MetricSender {

    private CounterRegistry registry;

    public MetricSender(CounterRegistry registry) {
        this.registry = registry;
    }

    public Counter send(Metric metric) {
        return registry.increment(metric);
    }
}
