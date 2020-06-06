package org.gaussian.amplifix.toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.annotation.DropRegister;
import org.gaussian.amplifix.toolkit.datagrid.DropRegistry;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.util.Arrays.asList;
import static org.gaussian.amplifix.toolkit.json.JsonCodec.decode;


@Slf4j
public class DropProcessor extends EventProcessor {

    public DropProcessor() {
        super(DropRegister.class);
    }

    public List<Meter> getData(Event event) {

        List<Meter> meters = new ArrayList<>();
        Optional<MetricAnnotation> metadata = lookupAnnotation(event);

        if (metadata.isPresent()) {
            DropRegistry dropRegistry = decode(event.data.encode(), DropRegistry.class);
            List<Tag> tags = getTagsForMetric(dropRegistry.tags);
            String metricName = decorateMetricName(DEFAULT_PREFIX, dropRegistry.metricName);
            meters = asList(counter(metricName, tags));
        }

        return meters;
    }

}
