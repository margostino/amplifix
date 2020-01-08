package toolkit.metric;

import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.ConversionControl;
import toolkit.annotation.Drop;
import toolkit.datagrid.DataGridNode;
import toolkit.datagrid.DropRegistry;
import toolkit.eventbus.ConversionEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static toolkit.util.GenericsUtils.getField;
import static toolkit.util.MetricUtils.decorateMetricName;


@Slf4j
public class ConversionHandler<T> {

    public final String prefix;
    public DataGridNode dataGridNode;

    public ConversionHandler(String prefix, DataGridNode dataGridNode) {
        this.prefix = prefix;
        this.dataGridNode = dataGridNode;
    }

    public List<Metric> process(DropRegistry dropRegistry) {
        List<String> tags = dropRegistry.tags;
        String metricName = decorateMetricName(prefix, dropRegistry.metricName);
        return asList(new Metric(metricName, tags.stream().map(String::toLowerCase).collect(toList())));
    }

    /**
     * Register a drop in data grid.
     *
     * @param event
     * @param annotation
     */
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

    /**
     * Evaluate event state and remove a drop in data grid.
     *
     * @param conversionEvent
     */
    public void check(ConversionEvent conversionEvent) {
        List<Field> fields = asList(conversionEvent.event.getClass().getFields());

        Optional<Field> fieldControl = fields.stream()
                                             .filter(field -> asList(field.getAnnotations()).stream()
                                                                                            .anyMatch(annotation -> annotation instanceof ConversionControl))
                                             .findFirst();

        if (fieldControl.isPresent()) {
            try {
                Field field = fieldControl.get();
                ConversionControl annotation = field.getAnnotation(ConversionControl.class);
                String eventValue = fieldControl.get().get(conversionEvent.event).toString();
                String fieldControlValue = annotation.value();

                if (fieldControlValue.equalsIgnoreCase(eventValue)) {
                    dataGridNode.remove(conversionEvent.conversionKey);
                }
            } catch (IllegalAccessException e) {
                // TODO
            }
        }
    }

}
