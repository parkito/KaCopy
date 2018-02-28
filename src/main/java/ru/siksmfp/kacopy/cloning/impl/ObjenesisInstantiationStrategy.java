package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IInstantiationStrategy;
import ru.siksmfp.kacopy.objenesis.Objenesis;
import ru.siksmfp.kacopy.objenesis.ObjenesisStd;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class ObjenesisInstantiationStrategy implements IInstantiationStrategy {
    private final Objenesis objenesis = new ObjenesisStd();

    public <T> T newInstance(Class<T> c) {
        return objenesis.newInstance(c);
    }

    private static ObjenesisInstantiationStrategy instance = new ObjenesisInstantiationStrategy();

    public static ObjenesisInstantiationStrategy getInstance() {
        return instance;
    }
}
