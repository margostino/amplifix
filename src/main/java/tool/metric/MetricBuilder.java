package tool.metric;

import java.util.ArrayList;
import java.util.List;

public class MetricBuilder {

    public Metric build(String event) {
        // TODO: build tags
        final List<String> tags = new ArrayList<>();
        tags.add("key.metric");
        tags.add("value.metric");

        return new Metric("demo.amplifix", tags);
    }
}
