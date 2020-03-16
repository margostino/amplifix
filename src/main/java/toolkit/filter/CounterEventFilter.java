package toolkit.filter;

import toolkit.annotation.Counter;

public class CounterEventFilter<E> extends EventFilter<E, Counter> {

    // TODO: evaluate if sub-classes is really necessary. Are there more responsibilities here?
    public CounterEventFilter() {
        super(Counter.class);
    }

}
