package org.gaussian.amplifix.toolkit.worker;

public interface ActionExecutor<I> {

    void execute(I input);
}
