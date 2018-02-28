package ru.siksmfp.kacopy.cloning.api;

import java.lang.reflect.Field;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public interface ICloningStrategy {
    enum Strategy {
        NULL_INSTEAD_OF_CLONE, // return null instead of a clone
        SAME_INSTANCE_INSTEAD_OF_CLONE, // return same instance instead of a clone
        IGNORE // ignore this strategy for this instance
    }

    Strategy strategyFor(Object toBeCloned, Field field);
}
