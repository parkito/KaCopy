package ru.siksmfp.kacopy.instanter.types;

import ru.siksmfp.kacopy.instanter.api.Instanter;
import ru.siksmfp.kacopy.instanter.api.InstantiatorStrategy;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class to extend if you want to have a class providing your own default strategy. Can also be
 * instantiated directly.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 */
public abstract class InstanterBase implements Instanter {

    /**
     * Strategy used by this Objenesi implementation to create classes
     */
    protected final InstantiatorStrategy strategy;

    /**
     * Strategy cache. Key = Class, Value = InstantiatorStrategy
     */
    protected ConcurrentHashMap<String, ObjectInstantiator<?>> cache;

    /**
     * Constructor allowing to pick a strategy and using cache
     *
     * @param strategy Strategy to use
     */

    public InstanterBase(InstantiatorStrategy strategy) {
        this(strategy, true);
    }

    /**
     * Flexible constructor allowing to pick the strategy and if caching should be used
     *
     * @param strategy Strategy to use
     * @param useCache If {@link ObjectInstantiator}s should be cached
     */
    public InstanterBase(InstantiatorStrategy strategy, boolean useCache) {
        if (strategy == null) {
            throw new IllegalArgumentException("A strategy can't be null");
        }
        this.strategy = strategy;
        this.cache = useCache ? new ConcurrentHashMap<>() : null;
    }

    /**
     * Will create a new object without any constructor being called
     *
     * @param clazz Class to instantiate
     * @return New instance of clazz
     */
    public <T> T newInstance(Class<T> clazz) {
        return getInstantiatorOf(clazz).newInstance();
    }

    /**
     * Will pick the best instantiator for the provided class. If you need to create a lot of
     * instances from the same class, it is way more efficient to create them from the same
     * ObjectInstantiator than calling {@link #newInstance(Class)}.
     *
     * @param clazz Class to instantiate
     * @return Instantiator dedicated to the class
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz) {
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("Primitive types can't be instantiated in Java");
        }
        if (cache == null) {
            return strategy.newInstantiatorOf(clazz);
        }
        ObjectInstantiator<?> instantiator = cache.get(clazz.getName());
        if (instantiator == null) {
            ObjectInstantiator<?> newInstantiator = strategy.newInstantiatorOf(clazz);
            instantiator = cache.putIfAbsent(clazz.getName(), newInstantiator);
            if (instantiator == null) {
                instantiator = newInstantiator;
            }
        }
        return (ObjectInstantiator<T>) instantiator;
    }

    @Override
    public String toString() {
        return getClass().getName() + " using " + strategy.getClass().getName()
                + (cache == null ? " without" : " with") + " caching";
    }
}
