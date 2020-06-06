package org.gaussian.amplifix.toolkit.metric;

import org.gaussian.amplifix.toolkit.model.Event;
import org.gaussian.amplifix.toolkit.processor.EventProcessor;

import java.util.List;

public abstract class AsyncWorker<D> {

    protected final List<EventProcessor> processors;

    public AsyncWorker(List<EventProcessor> processors) {
        this.processors = processors;
    }

    public abstract void execute(Event event);

}
