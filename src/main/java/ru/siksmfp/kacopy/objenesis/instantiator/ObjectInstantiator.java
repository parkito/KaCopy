package ru.siksmfp.kacopy.objenesis.instantiator;

/**
 * Instantiates a new object.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public interface ObjectInstantiator<T> {

    /**
     * Returns a new instance of an object. The returned object's class is defined by the
     * implementation.
     *
     * @return A new instance of an object.
     */
    T newInstance();

}
