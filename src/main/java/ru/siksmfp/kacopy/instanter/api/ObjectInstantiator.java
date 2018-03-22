package ru.siksmfp.kacopy.instanter.api;

/**
 * Instantiates a new object.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
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
