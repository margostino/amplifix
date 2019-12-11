package tool.metric;

import lombok.Data;

import java.util.List;

import static java.text.MessageFormat.format;

@Data
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
