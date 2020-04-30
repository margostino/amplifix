package org.gaussian.amplifix.toolkit.metric;

import io.micrometer.core.instrument.Meter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.eventbus.Event;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Data
public class MetricBuilder<E> {

    private final List<EventProcessor> processors;

    public MetricBuilder(List<EventProcessor> processors) {
        this.processors = processors;
    }

    public List<Meter> build(Event event) {

        Stream<Meter> stream = processors.stream()
                                         .map(processor -> processor.process(event))
                                         .flatMap(List::stream)
                                         .map(Meter.class::cast);

        return stream.collect(toList());

    }

}
