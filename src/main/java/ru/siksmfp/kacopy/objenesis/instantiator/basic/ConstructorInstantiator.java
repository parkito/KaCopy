package ru.siksmfp.kacopy.objenesis.instantiator.basic;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

import java.lang.reflect.Constructor;

/**
 * Instantiates a class by grabbing the no args constructor and calling Constructor.newInstance().
 * This can deal with default public constructors, but that's about it.
 *
 * @param <T> Type instantiated
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see ObjectInstantiator
 */
@Instantiator(Typology.NOT_COMPLIANT)
public class ConstructorInstantiator<T> implements ObjectInstantiator<T> {

    protected Constructor<T> constructor;

    public ConstructorInstantiator(Class<T> type) {
        try {
            constructor = type.getDeclaredConstructor((Class[]) null);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }

    public T newInstance() {
        try {
            return constructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }

}
