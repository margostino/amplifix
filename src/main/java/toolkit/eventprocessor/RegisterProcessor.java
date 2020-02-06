package toolkit.eventprocessor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Drop;
import toolkit.datagrid.DataGridNode;
import toolkit.datagrid.DropRegistry;
import toolkit.metric.TagSerializable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static toolkit.util.GenericsUtils.getField;


@Slf4j
public class RegisterProcessor<E> extends EventProcessor<E> {

    public DataGridNode dataGridNode;

    public RegisterProcessor(String prefix, DataGridNode dataGridNode) {
        super(prefix, Drop.class);
        this.dataGridNode = dataGridNode;
    }

    protected List<Meter> getMeters(E event) {
        Drop annotationDrop = (Drop) getAnnotation(event);
        String dropKey = annotationDrop.field();
        String eventControl = annotationDrop.event();
        List<String> tagKeys = asList(annotationDrop.tags());
        String metricName = annotationDrop.metricName();
        List<Tag> tags = new ArrayList<>();
        long ttl = annotationDrop.ttl();
        TimeUnit timeUnit = annotationDrop.timeUnit();
        Field field = getField(event, dropKey);

        try {
            String value = (String) field.get(event);

            for (String tagKey : tagKeys) {
                tags.add(new TagSerializable(tagKey, (String) getField(event, tagKey).get(event)));
            }

            DropRegistry dropRegistry = new DropRegistry(metricName, dropKey, value, tags, eventControl, now());
            dataGridNode.put(value, dropRegistry, ttl, timeUnit);
        } catch (IllegalAccessException e) {
            // TODO
        }

        return emptyList();

    }

}
