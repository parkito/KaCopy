package ru.siksmfp.kacopy.instanter.basic;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;

import java.lang.reflect.Constructor;

/**
 * Instantiates a class by grabbing the no args constructor and calling Constructor.newInstance().
 * This can deal with default public constructors, but that's about it.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 * @see ObjectInstantiator
 */
@Instantiator(Typology.NOT_COMPLIANT)
public class ConstructorInstantiator<T> implements ObjectInstantiator<T> {

    protected Constructor<T> constructor;

    public ConstructorInstantiator(Class<T> type) {
        try {
            constructor = type.getDeclaredConstructor((Class[]) null);
        } catch (Exception e) {
            throw new InstanterException(e);
        }
    }

    public T newInstance() {
        try {
            return constructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new InstanterException(e);
        }
    }

}
