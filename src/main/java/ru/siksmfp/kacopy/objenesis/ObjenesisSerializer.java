package ru.siksmfp.kacopy.objenesis;

import ru.siksmfp.kacopy.objenesis.strategy.SerializingInstantiatorStrategy;

/**
 * Objenesis implementation using the {@link SerializingInstantiatorStrategy}.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class ObjenesisSerializer extends ObjenesisBase {

    /**
     * Default constructor using the {@link org.objenesis.strategy.SerializingInstantiatorStrategy}
     */
    public ObjenesisSerializer() {
        super(new SerializingInstantiatorStrategy());
    }

    /**
     * Instance using the {@link org.objenesis.strategy.SerializingInstantiatorStrategy} with or without caching
     * {@link org.objenesis.instantiator.ObjectInstantiator}s
     *
     * @param useCache If {@link org.objenesis.instantiator.ObjectInstantiator}s should be cached
     */
    public ObjenesisSerializer(boolean useCache) {
        super(new SerializingInstantiatorStrategy(), useCache);
    }
}
