package toolkit.metric;

import io.micrometer.core.instrument.Meter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import toolkit.datagrid.DataGridNode;
import toolkit.eventbus.Event;
import toolkit.processor.ConversionProcessor;
import toolkit.processor.CounterProcessor;
import toolkit.processor.DropProcessor;
import toolkit.processor.EventProcessor;
import toolkit.processor.RegisterProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Data
public class MetricBuilder<E> {

    private final String prefix;
    private final List<EventProcessor> processors;

    public MetricBuilder(String prefix, DataGridNode dataGridNode) {
        this.prefix = isNull(prefix) ? "" : prefix;
        this.processors = new ArrayList<>();
        processors.add(new CounterProcessor(this.prefix));
        processors.add(new RegisterProcessor(this.prefix, dataGridNode));
        processors.add(new ConversionProcessor(this.prefix, dataGridNode));
        processors.add(new DropProcessor(this.prefix));
    }

    public List<Meter> build(Event event) {

        Stream<Meter> stream = processors.stream()
                                         .map(processor -> processor.process(event))
                                         .flatMap(List::stream)
                                         .map(Meter.class::cast);

        return stream.collect(toList());

    }

}
