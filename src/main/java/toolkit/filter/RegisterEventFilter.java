package toolkit.filter;

import toolkit.annotation.Drop;

public class RegisterEventFilter<E> extends EventFilter<E, Drop> {

    // TODO: evaluate if sub-classes is really necessary. Are there more responsibilities here?
    public RegisterEventFilter() {
        super(Drop.class);
    }

}
