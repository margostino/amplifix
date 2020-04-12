package toolkit.processor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Counter;
import toolkit.eventbus.Event;
import toolkit.metadatareader.CounterAnnotation;
import toolkit.metadatareader.MetricAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.micrometer.core.instrument.Metrics.counter;
import static java.util.Arrays.asList;
import static java.util.Collections.frequency;
import static java.util.stream.Collectors.toList;
import static toolkit.util.StringUtils.toSnakeCase;


@Slf4j
public class CounterProcessor extends EventProcessor {

    public CounterProcessor() {
        super(Counter.class);
    }

    protected List<Meter> getMeters(Event event) {

        List<Meter> meters = new ArrayList<>();

        Optional<MetricAnnotation> metadata = lookupAnnotation(event);

        if (metadata.isPresent()) {

            CounterAnnotation counter = (CounterAnnotation) metadata.get();
            JsonObject data = event.data;

            String metricName = decorateMetricName(DEFAULT_PREFIX, counter.metricName);

            List<String> fields = counter.fields;

            List<Tag> tags = fields.stream()
                                   .map(field -> getTag(data, field))
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

            meters = multiTags.stream()
                              .map(tag -> getMultiTags(metricName, simpleTags, tag))
                              .collect(toList());

        }

        return meters;
    }

    private Meter getMultiTags(String metricName, List<Tag> simpleTags, Tag tag) {
        List<Tag> tags = new ArrayList<>();
        tags.addAll(simpleTags);
        tags.add(Tag.of(tag.getKey(), tag.getValue()));
        return counter(metricName, tags);

    }

    private List<Tag> getTag(JsonObject data, String field) {

        String key = toSnakeCase(field);
        if (data.getValue(field) instanceof JsonArray) {
            JsonArray values = data.getJsonArray(field);
            return values.stream()
                         .map(value -> Tag.of(key, String.valueOf(value)))
                         .collect(toList());
        } else {
            return asList(Tag.of(key, (String) data.getValue(field)));
        }
    }

//    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
//        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//    }

}
