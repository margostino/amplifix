package toolkit.eventprocessor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Counter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.util.Arrays.asList;
import static java.util.Collections.frequency;
import static java.util.stream.Collectors.toList;
import static toolkit.util.GenericsUtils.getFields;
import static toolkit.util.GenericsUtils.isList;


@Slf4j
public class CounterProcessor<E> extends EventProcessor<E> {

    public CounterProcessor(String prefix) {
        super(prefix, Counter.class);
    }

    protected List<Meter> getMeters(E event) {

        Counter counter = (Counter) eventFilter.getAnnotation(event);

        String metricName = decorateMetricName(prefix, counter.metricName());
        List<String> fieldNames = asList(counter.fields());

        List<Field> fields = getFields(event, fieldNames);

        List<Tag> tags = fields.stream()
                               .map(field -> getTag(event, field))
                               .flatMap(List::stream)
                               .collect(toList());

        List<String> keys = tags.stream()
                                .map(Tag::getKey)
                                .collect(toList());

        List<Tag> simpleTags = tags.stream()
                                   .filter(tag -> frequency(keys, tag.getKey()) == 1)
                                   .collect(toList());

        List<Tag> multiTags = tags.stream()
                                  .filter(tag -> frequency(keys, tag.getKey()) > 1)
                                  .collect(toList());

        return multiTags.stream()
                        .map(tag -> getMultiTags(metricName, simpleTags, tag))
                        .collect(toList());

    }

    private Meter getMultiTags(String metricName, List<Tag> simpleTags, Tag tag) {
        List<Tag> tags = new ArrayList<>();
        tags.addAll(simpleTags);
        tags.add(Tag.of(tag.getKey(), tag.getValue()));
        return counter(metricName, tags);

    }

    private List<Tag> getTag(E event, Field field) {

        String key = getTagKey(field);

        try {

            if (isList(event, field)) {
                List<Object> values = (List) field.get(event);
                return values.stream()
                             .map(value -> Tag.of(key, String.valueOf(value)))
                             .collect(toList());
            } else {
                return asList(Tag.of(key, (String) field.get(event)));
            }

        } catch (IllegalAccessException e) {
            // TODO
            return null;
        }


    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
