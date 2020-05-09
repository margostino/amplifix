package org.gaussian.amplifix.toolkit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Drop {

    String id() default "drop";

    String metricName() default "";

    String[] tags();

    DropConfig config();
}
