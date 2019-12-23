package toolkit.metric;

import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class MetricSender {

    private CounterRegistry registry;

    public MetricSender(CounterRegistry registry) {
        this.registry = registry;
    }

    public List<Counter> send(List<Metric> metrics) {
        return metrics.stream()
                      .map(registry::increment)
                      .collect(toList());
    }
}
