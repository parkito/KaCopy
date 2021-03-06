package ru.siksmfp.kacopy.instanter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denote that the class in an instantiator of a given type
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Instantiator {

    /**
     * @return type of instantiator
     */
    Typology value();
}
