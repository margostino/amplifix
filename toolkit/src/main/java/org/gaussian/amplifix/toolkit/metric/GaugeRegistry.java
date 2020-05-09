package org.gaussian.amplifix.toolkit.metric;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import org.gaussian.amplifix.toolkit.model.Metric;
import org.gaussian.amplifix.toolkit.model.TagMetric;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

public class GaugeRegistry {

    public AtomicInteger update(Metric metric) {
        //Gauge
        Tag tag = new TagMetric("one", "two");
        List<Tag> tags = asList(tag);
        return Metrics.gauge(metric.name(), tags, new AtomicInteger(1));
    }
}
