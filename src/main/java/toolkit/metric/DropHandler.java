package toolkit.metric;

import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Drop;
import toolkit.datagrid.DataGridNode;
import toolkit.datagrid.DropRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


@Slf4j
public class DropHandler<T> extends MetricHandler<T> {

    public DataGridNode dataGridNode;

    public DropHandler(String prefix, DataGridNode dataGridNode) {
        super(prefix);
        this.dataGridNode = dataGridNode;
    }

    public List<Metric> process(T event, Annotation annotation) {
        DropRegistry dropRegistry = (DropRegistry) event;
        List<String> tags = dropRegistry.tags;
        String metricName = decorateMetricName(prefix, dropRegistry.metricName);
        return asList(new Metric(metricName, tags.stream().map(String::toLowerCase).collect(toList())));
    }

    public void register(T event, Annotation annotation) {
        Drop annotationDrop = (Drop) annotation;
        String key = annotationDrop.field();
        String eventControl = annotationDrop.event();
        List<String> tagKeys = asList(annotationDrop.tags());
        String metricName = annotationDrop.metricName();
        List<String> tags = new ArrayList<>();
        long ttl = annotationDrop.ttl();
        TimeUnit timeUnit = annotationDrop.timeUnit();
        Field field = getField(event, key);

        try {
            String value = (String) field.get(event);

            for (String tagKey : tagKeys) {
                tags.add(tagKey);
                tags.add((String) getField(event, tagKey).get(event));
            }

            DropRegistry dropRegistry = new DropRegistry(metricName, key, value, tags, eventControl, now());
            dataGridNode.put(value, dropRegistry, ttl, timeUnit);
        } catch (IllegalAccessException e) {
            // TODO
        }

    }

}
