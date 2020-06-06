package org.gaussian.amplifix.toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.annotation.ConversionRegister;
import org.gaussian.amplifix.toolkit.datagrid.DataGridNode;
import org.gaussian.amplifix.toolkit.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.util.Arrays.asList;


@Slf4j
public class ConversionProcessor extends EventProcessor {

    public DataGridNode dataGridNode;

    public ConversionProcessor(DataGridNode dataGridNode) {
        super(ConversionRegister.class);
        this.dataGridNode = dataGridNode;
    }

    protected List<Meter> getData(Event event) {

        List<Meter> meters = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        JsonObject metadata = null;//event.metadata.control;
        JsonObject data = event.data;
        // TODO: evaluate decode in a class
        JsonObject control = metadata.getJsonObject("control");
        String key = metadata.getString("key");
        String metricName = decorateMetricName(DEFAULT_PREFIX, "conversion");

        Optional<String> fieldName = control.fieldNames()
                                            .stream()
                                            .findFirst();

        if (fieldName.isPresent()) {
            String value = control.getString(fieldName.get());
            String valueToControl = data.getString(fieldName.get());
            if (valueToControl.equalsIgnoreCase(value)) {
                dataGridNode.remove(key);
            }

            meters = asList(counter(metricName, tags));
        }

        return meters;
    }

}
