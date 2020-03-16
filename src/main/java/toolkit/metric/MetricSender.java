package toolkit.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Component;

import static io.micrometer.core.instrument.Meter.Type.COUNTER;

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

        if (meter.getId().getType().equals(COUNTER)) {
            ((Counter) meter).increment();
        }
    }

    private Counter getCounter(Metric metric) {
        return Metrics.counter(metric.name(), metric.getTags());
    }

}
