package toolkit.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CounterRegistry {

    private MeterRegistry registry;

    public CounterRegistry(MeterRegistry registry) {
        this.registry = registry;
    }

    public Counter increment(Metric metric) {
        Counter counter = Counter
                .builder(metric.name())
                .tags(metric.getTags())
                .register(this.registry);
        counter.increment();
        return counter;
    }

}