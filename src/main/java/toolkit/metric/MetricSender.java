package toolkit.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Component;

@Component
public class MetricSender {

    private CounterRegistry counter;
    private GaugeRegistry gauge;

    public MetricSender() {
        this.counter = new CounterRegistry();
        this.gauge = new GaugeRegistry();
    }

    public void send(Meter meter) {
        //gauge.update(metrics.get(0));

//        metrics.stream()
//               .map(this::getCounter)
//               .forEach(counter -> counter.increment());
        return;
    }

    private Counter getCounter(Metric metric) {
        return Metrics.counter(metric.name(), metric.getTags());
    }

}
