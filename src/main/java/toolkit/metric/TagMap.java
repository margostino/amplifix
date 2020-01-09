package toolkit.metric;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static toolkit.util.GenericsUtils.isList;
import static toolkit.util.MetricUtils.getTagKey;

@Data
public class TagMap {

    public final String key;
    public final List<String> values;

    private TagMap(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public static Builder of(Field field) {
        return new Builder(field);
    }

    public static class Builder<E> {

        private E event;
        private Field field;

        public Builder(Field field) {
            this.field = field;
        }

        public Builder from(E event) {
            this.event = event;
            return this;
        }

        public TagMap build() {
            String tagKey = getTagKey(field);
            List<String> tagValues = new ArrayList<>();

            try {
                if (isList(event, field)) {
                    List<Object> values = (List) field.get(event);
                    tagValues = values.stream()
                                      .map(String::valueOf)
                                      .collect(toList());
                } else {
                    tagValues = asList((String) field.get(event));
                }

            } catch (IllegalAccessException e) {
                // TODO
            }

            return new TagMap(tagKey, tagValues);
        }

    }

}