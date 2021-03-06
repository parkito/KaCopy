package ru.siksmfp.kacopy.instanter.api;

/**
 * Defines a strategy to determine the best instantiator for a class.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
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
