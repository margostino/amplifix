package org.gaussian.amplifix.toolkit.metric;

import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;

import java.util.List;

@Slf4j
public class TraceWorker<D> extends AsyncWorker<D> {

    public TraceWorker(List<EventProcessor> processors) {
        super(processors);
    }

    public void execute(Event event) {
        processors.stream()
                  .map(processor -> processor.process(event))
                  .map(String.class::cast)
                  .forEach(LOG::info);
    }

}
