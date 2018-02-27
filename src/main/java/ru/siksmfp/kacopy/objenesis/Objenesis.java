package ru.siksmfp.kacopy.objenesis;

import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;

/**
 * Common interface to all kind of Objenesis objects
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public interface Objenesis {

    /**
     * Will create a new object without any constructor being called
     *
     * @param <T>   Type instantiated
     * @param clazz Class to instantiate
     * @return New instance of clazz
     */
    <T> T newInstance(Class<T> clazz);

    /**
     * Will pick the best instantiator for the provided class. If you need to create a lot of
     * instances from the same class, it is way more efficient to create them from the same
     * ObjectInstantiator than calling {@link #newInstance(Class)}.
     *
     * @param <T>   Type to instantiate
     * @param clazz Class to instantiate
     * @return Instantiator dedicated to the class
     */
    <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz);
}
