package toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.DropRegister;
import toolkit.datagrid.DropRegistry;
import toolkit.eventbus.Event;
import toolkit.metadatareader.MetricAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.util.Arrays.asList;
import static toolkit.json.JsonCodec.decode;


@Slf4j
public class DropProcessor extends EventProcessor {

    public DropProcessor(String prefix) {
        super(prefix, DropRegister.class);
    }

    public List<Meter> getMeters(Event event) {

        List<Meter> meters = new ArrayList<>();
        Optional<MetricAnnotation> metadata = lookupAnnotation(event);

        if (metadata.isPresent()) {
            DropRegistry dropRegistry = decode(event.data.encode(), DropRegistry.class);
            List<Tag> tags = getTagsForMetric(dropRegistry.tags);
            String metricName = decorateMetricName(prefix, dropRegistry.metricName);
            meters = asList(counter(metricName, tags));
        }

        return meters;
    }

}
