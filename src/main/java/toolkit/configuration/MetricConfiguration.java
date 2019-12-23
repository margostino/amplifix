package toolkit.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import toolkit.metric.MetricBuilder;

import java.util.List;

@AllArgsConstructor
@Data
public class MetricConfiguration {

    public List<String> commonTags;
    public MetricBuilder metricBuilder;
}
