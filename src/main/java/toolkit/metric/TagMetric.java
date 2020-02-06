package toolkit.metric;

import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TagMetric implements Tag {

    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
