package org.gaussian.amplifix.toolkit.metric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

import static java.text.MessageFormat.format;

@Getter
@AllArgsConstructor
public class Metric {

    private final String name;
    private final List<String> tags;

    public String[] getTags() {
        return tags.toArray(new String[0]);
    }

    public String getName(String prefix) {
        return format("{0}.{1}", prefix, name);
    }
}
