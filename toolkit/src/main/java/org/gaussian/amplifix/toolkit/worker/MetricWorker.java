package org.gaussian.amplifix.toolkit.worker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;

import java.util.List;

import static io.micrometer.core.instrument.Meter.Type.COUNTER;
import static java.util.stream.Collectors.toList;

public class MetricWorker<D> extends AsyncWorker<D> {

    public MetricWorker(List<EventProcessor> processors) {
//        this.counter = new CounterRegistry();
//        this.gauge = new GaugeRegistry();
        super(processors);
    }

    public void execute(Event event) {

        List<Meter> meters = processors.stream()
                                       .map(processor -> processor.process(event))
                                       .map(Meter.class::cast)
                                       .collect(toList());

        meters.stream()
              .filter(meter -> meter != null && meter.getId().getType().equals(COUNTER))
              .map(Counter.class::cast)
              .forEach(Counter::increment);
    }

}
