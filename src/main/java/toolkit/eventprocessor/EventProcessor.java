package toolkit.eventprocessor;

import io.micrometer.core.instrument.Meter;
import toolkit.eventfilter.EventFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public abstract class EventProcessor<E> {

    protected final String prefix;
    protected final EventFilter<E, ? extends Annotation> eventFilter;

    public EventProcessor(String prefix, Class<? extends Annotation> annotation) {
        this.prefix = prefix;
        this.eventFilter = new EventFilter(annotation);
    }

    abstract List<Meter> getMeters(E event);

    public Annotation getAnnotation(E event) {
        return eventFilter.getAnnotation(event);
    }


    protected boolean filter(E event) {
        return asList(event.getClass().getAnnotations())
                .stream()
                .anyMatch(eventFilter::filter);
    }

    public List<Meter> process(E event) {
        return filter(event) ? getMeters(event) : emptyList();
    }

    protected String decorateMetricName(String prefix, String metricName) {
        return isNullOrEmpty(prefix) ? metricName : format("{0}.{1}", prefix, metricName);
    }

    protected String getTagKey(Field field) {
        return toSnakeCase(field.getName());
    }

    protected String toSnakeCase(String fieldName) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, fieldName);
    }

}
