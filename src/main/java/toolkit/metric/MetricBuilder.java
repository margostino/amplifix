package toolkit.metric;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import toolkit.annotation.Counter;
import toolkit.annotation.Drop;
import toolkit.annotation.DropRegistryControl;
import toolkit.datagrid.DataGridNode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Data
public class MetricBuilder<T> {

    private final String prefix;
    private final MetricHandler counterHandler;
    private final DropHandler dropHandler;

    public MetricBuilder(String prefix, DataGridNode dataGridNode) {
        this.prefix = isNull(prefix) ? "" : prefix;
        this.counterHandler = new CounterHandler(this.prefix);
        this.dropHandler = new DropHandler(this.prefix, dataGridNode);
    }

    public List<Metric> build(T event) {

        List<Metric> metrics = new ArrayList<>();

        Annotation[] annotations = getAnnotations(event);
        for (Annotation annotation : annotations) {
            // TODO: first iteration only Counter is enabled (others could be Gauge)
            if (annotation instanceof Counter) {
                metrics.addAll(counterHandler.process(event, annotation));
            }

            if (annotation instanceof Drop) {
                dropHandler.register(event, annotation);
            }

            if (annotation instanceof DropRegistryControl) {
                metrics.addAll(dropHandler.process(event, annotation));
            }
        }

        return metrics;
    }

    private Annotation[] getAnnotations(T event) {
        return event.getClass().getAnnotations();
    }

}
