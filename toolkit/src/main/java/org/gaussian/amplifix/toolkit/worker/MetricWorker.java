package org.gaussian.amplifix.toolkit.worker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static io.micrometer.core.instrument.Meter.Type.COUNTER;

public class MetricWorker<D> extends AsyncWorker<D> {

    public MetricWorker(List<EventProcessor> processors) {
//        this.counter = new CounterRegistry();
//        this.gauge = new GaugeRegistry();
        super(processors);
    }

    public void execute(Event event) {
        processors.stream()
                  .map(processor -> processor.process(event))
                  .filter(Objects::nonNull)
                  .map(List.class::cast)
                  .flatMap(Collection::stream)
                  .map(Meter.class::cast)
                  .filter(this::filterByCounter)
                  .map(Counter.class::cast)
                  .forEach(Counter::increment);
    }

    private boolean filterByCounter(Meter meter) {
        return meter != null && meter.getId().getType().equals(COUNTER);
    }

}
