package toolkit.metric;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Counter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


@Slf4j
public class CounterHandler<T> extends MetricHandler<T> {

    public CounterHandler(String prefix) {
        super(prefix);
    }

    @AllArgsConstructor
    public class Tag {
        public String key;
        public String value;
    }

    public List<Metric> build(T event, Annotation annotation) {
        Counter annotationCounter = (Counter) annotation;
        String metricName = decorateMetricName(prefix, annotationCounter.metricName());
        List<String> fieldNames = asList(annotationCounter.fields());

        List<Field> fields = fieldNames.stream()
                                       .map(fieldName -> getField(event, fieldName))
                                       .collect(toList());

        List<String> simpleTags = fields.stream()
                                        .filter(field -> !isList(event, field))
                                        .map(field -> processSimpleTags(event, field))
                                        .flatMap(List::stream)
                                        .collect(toList());


        List<Tag> nestedTags = fields.stream()
                                     .filter(field -> isList(event, field))
                                     .map(field -> processNestedTags(event, field))
                                     .flatMap(List::stream)
                                     .collect(toList());

        List<Metric> metrics = nestedTags.stream()
                                         .map(nestedTag -> processTags(metricName, simpleTags, nestedTag))
                                         .collect(toList());

        return metrics;
    }

    private List<String> processSimpleTags(T event, Field field) {
        String key = getTagKey(field);
        String value;
        List<String> tags = new ArrayList<>();

        try {
            value = ((String) field.get(event));
        } catch (IllegalAccessException e) {
            value = null;
        }
        tags.add(key);
        tags.add(value);
        return tags;
    }

    private List<Tag> processNestedTags(T event, Field field) {
        String key = getTagKey(field);
        List<Tag> tags = new ArrayList<>();

        try {
            List<Object> values = (List) field.get(event);
            tags = values.stream()
                         .map(value -> value.toString())
                         .map(value -> new Tag(key, value))
                         .collect(toList());

        } catch (IllegalAccessException e) {
            // TODO
        }

        return tags;
    }

    private Metric processTags(String metricName, List<String> simpleTags, Tag nestedTag) {
        List<String> tags = new ArrayList<>();
        tags.addAll(simpleTags);
        tags.add(nestedTag.key);
        tags.add(nestedTag.value);
        return new Metric(metricName, tags.stream().map(String::toLowerCase).collect(toList()));
    }
}
