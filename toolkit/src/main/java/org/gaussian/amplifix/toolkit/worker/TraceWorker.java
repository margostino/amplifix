package org.gaussian.amplifix.toolkit.worker;

import lombok.extern.slf4j.Slf4j;
import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;

import java.util.List;
import java.util.Objects;

@Slf4j
public class TraceWorker<D> extends AsyncWorker<D> {

    public TraceWorker(List<EventProcessor> processors) {
        super(processors);
    }

    public void execute(Event event) {
        processors.stream()
                  .map(processor -> processor.process(event))
                  .filter(Objects::nonNull)
                  .map(String.class::cast)
                  .forEach(LOG::info);
    }

}
