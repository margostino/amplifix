package toolkit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Drop {

    String id() default "drop";

    String metricName() default "";

    String field() default "";

    String event() default "";

    long ttl();

    TimeUnit timeUnit();

    String[] tags();
}
