package toolkit.metric;

import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Counter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static toolkit.util.GenericsUtils.getFields;
import static toolkit.util.MetricUtils.decorateMetricName;


@Slf4j
public class CounterHandler<T> {

    public final String prefix;

    public CounterHandler(String prefix) {
        this.prefix = prefix;
    }

    public List<Metric> process(T event, Counter annotation) {
        String metricName = decorateMetricName(prefix, annotation.metricName());
        List<String> fieldNames = asList(annotation.fields());

        List<Field> fields = getFields(event, fieldNames);

        List<TagMap> tagMaps = fields.stream()
                                     .map(field -> TagMap.of(field).from(event).build())
                                     .collect(toList());

        List<TagMap> simpleTags = tagMaps.stream()
                                         .filter(tagMap -> tagMap.values.size() == 1)
                                         .collect(toList());

        List<TagMap> multiTags = tagMaps.stream()
                                        .filter(tagMap -> tagMap.values.size() > 1)
                                        .collect(toList());

        List<String> baseTags = simpleTags.stream()
                                          .map(this::getSimpleTags)
                                          .flatMap(List::stream)
                                          .collect(toList());

        return multiTags.stream()
                        .map(tagMap -> getMultiTags(metricName, baseTags, tagMap))
                        .flatMap(List::stream)
                        .collect(toList());

    }

    public List<Metric> getMultiTags(String metricName, List<String> baseTags, TagMap tagMap) {
        return tagMap.values().stream()
                     .map(value -> {
                         List<String> tags = new ArrayList<>();
                         tags.addAll(baseTags);
                         tags.add(tagMap.key);
                         tags.add(value);
                         return new Metric(metricName, tags);
                     })
                     .collect(toList());
    }

    private List<String> getSimpleTags(TagMap tagMap) {
        List<String> tags = new ArrayList<>();
        tags.add(tagMap.key());
        tags.add(tagMap.values().get(0));
        return tags;
    }

}
