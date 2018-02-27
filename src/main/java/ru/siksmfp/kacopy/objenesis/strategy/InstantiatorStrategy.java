package ru.siksmfp.kacopy.objenesis.strategy;

import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;

/**
 * Defines a strategy to determine the best instantiator for a class.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public interface InstantiatorStrategy {

    /**
     * Create a dedicated instantiator for the given class
     *
     * @param <T>  Type to instantiate
     * @param type Class that will be instantiated
     * @return Dedicated instantiator
     */
    <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type);
}
