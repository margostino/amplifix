package org.gaussian.amplifix.toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.annotation.Drop;
import org.gaussian.amplifix.toolkit.datagrid.DataGridNode;
import org.gaussian.amplifix.toolkit.datagrid.DropRegistry;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.metadatareader.DropAnnotation;
import org.gaussian.amplifix.toolkit.annotation.MetricAnnotation;
import org.gaussian.amplifix.toolkit.model.TagSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.time.Instant.now;
import static java.util.Arrays.asList;

@Slf4j
public class RegisterProcessor extends EventProcessor {

    public DataGridNode dataGridNode;

    public RegisterProcessor(DataGridNode dataGridNode) {
        super(Drop.class);
        this.dataGridNode = dataGridNode;
    }

    protected List<Meter> getData(Event event) {

        List<Meter> meters = new ArrayList<>();
        Optional<MetricAnnotation> metadata = lookupAnnotation(event);
        JsonObject data = event.data;

        if (metadata.isPresent()) {
            DropAnnotation drop = (DropAnnotation) metadata.get();
            String key = drop.field;
            String control = drop.event;
            List<String> tagKeys = drop.tags;
            String metricName = drop.metricName;
            String registerMetricName = decorateMetricName(DEFAULT_PREFIX, "register");
            long ttl = drop.ttl;
            TimeUnit timeUnit = drop.timeUnit;

            List<TagSerializable> tags = new ArrayList<>();
            String value = data.getString(key);

            for (String tagKey : tagKeys) {
                tags.add(new TagSerializable(tagKey, data.getString(tagKey)));
                DropRegistry dropRegistry = new DropRegistry(metricName, key, value, tags, control, now());
                dataGridNode.put(value, dropRegistry, ttl, timeUnit);
            }

            meters = asList(counter(registerMetricName, getTagsForMetric(tags)));
        }

        return meters;

    }

}
