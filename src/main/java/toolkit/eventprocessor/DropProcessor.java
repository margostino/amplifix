package toolkit.eventprocessor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.DropRegistryControl;
import toolkit.datagrid.DropRegistry;

import java.util.List;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.util.Arrays.asList;


@Slf4j
public class DropProcessor<E> extends EventProcessor<E> {

    public DropProcessor(String prefix) {
        super(prefix, DropRegistryControl.class);
    }

    public List<Meter> getMeters(E event) {
        DropRegistry dropRegistry = (DropRegistry) event;
        List<Tag> tags = dropRegistry.tags;
        String metricName = decorateMetricName(prefix, dropRegistry.metricName);
        return asList(counter(metricName, tags));
    }

}
