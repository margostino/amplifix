package toolkit.util;

import java.lang.reflect.Field;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.text.MessageFormat.format;

public class MetricUtils {

    public static String decorateMetricName(String prefix, String metricName) {
        return isNullOrEmpty(prefix) ? metricName : format("{0}.{1}", prefix, metricName);
    }

    public static String getTagKey(Field field) {
        return toSnakeCase(field.getName());
    }

    public static String toSnakeCase(String fieldName) {
        return UPPER_CAMEL.to(LOWER_UNDERSCORE, fieldName);
    }

}
