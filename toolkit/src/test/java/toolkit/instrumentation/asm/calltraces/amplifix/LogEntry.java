package toolkit.instrumentation.asm.calltraces.amplifix;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogEntry {
    boolean enter() default false;
    boolean exit() default false;
}

