package org.gaussian.amplifix.toolkit.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.gaussian.amplifix.toolkit.model.Metric;

public class CounterRegistry {

    public Counter increment(Metric metric) {
        Counter counter = Metrics.counter(metric.name(), metric.getTags());
        counter.increment();
        return counter;
    }

}
