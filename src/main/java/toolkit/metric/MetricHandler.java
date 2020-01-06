package toolkit.metric;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.text.MessageFormat.format;


public abstract class MetricHandler<T> {

    protected final String prefix;

    public MetricHandler(String prefix) {
        this.prefix = prefix;
    }

    protected abstract List<Metric> process(T event, Annotation annotation);

    protected String decorateMetricName(String prefix, String metricName) {
        return isNullOrEmpty(prefix) ? metricName : format("{0}.{1}", prefix, metricName);
    }

    protected String getTagKey(Field field) {
        return toSnakeCase(field.getName());
    }

    private String toSnakeCase(String fieldName) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, fieldName);
    }

    protected Field getField(T event, String fieldName) {
        try {
            return event.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            // TODO
            return null;
        }
    }

    private boolean isInstance(T event, Field field, Class clazz) throws IllegalAccessException {
        return clazz.isInstance(field.get(event));
    }

    protected Boolean isList(T event, Field field) {
        Type genericFieldType = field.getGenericType();

        try {
            return isInstance(event, field, List.class) && genericFieldType instanceof ParameterizedType;
        } catch (IllegalAccessException e) {
            // TODO
            return null;
        }

    }

}
