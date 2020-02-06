package toolkit.metric;

import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class TagSerializable implements Tag, Serializable {

    public final String key;
    public final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
