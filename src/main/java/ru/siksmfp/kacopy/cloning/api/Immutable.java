package ru.siksmfp.kacopy.cloning.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks the specific class as immutable and the cloner avoids cloning it
 *
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Immutable {
    /**
     * by default all subclasses of the @Immutable class are not immutable. This can override it.
     *
     * @return true for subclasses of @Immutable class to be regarded as immutable from the cloner
     */
    boolean subClass() default false;
}
