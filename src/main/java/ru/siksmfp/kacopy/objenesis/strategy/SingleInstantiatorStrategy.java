package ru.siksmfp.kacopy.objenesis.strategy;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Strategy returning only one instantiator type. Useful if you know on which JVM Instanter
 * will be used and want to specify it explicitly.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class SingleInstantiatorStrategy implements InstantiatorStrategy {

    private Constructor<?> constructor;

    /**
     * Create a strategy that will return always the same instantiator type. We assume this instantiator
     * has one constructor taking the class to instantiate in parameter.
     *
     * @param <T>          the type we want to instantiate
     * @param instantiator the instantiator type
     */
    public <T extends ObjectInstantiator<?>> SingleInstantiatorStrategy(Class<T> instantiator) {
        try {
            constructor = instantiator.getConstructor(Class.class);
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(e);
        }
    }

    /**
     * Return an instantiator for the wanted type and of the one and only type of instantiator returned by this
     * class.
     *
     * @param <T>  the type we want to instantiate
     * @param type Class to instantiate
     * @return The ObjectInstantiator for the class
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        try {
            return (ObjectInstantiator<T>) constructor.newInstance(type);
        } catch (InstantiationException e) {
            throw new ObjenesisException(e);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        } catch (InvocationTargetException e) {
            throw new ObjenesisException(e);
        }
    }
}
