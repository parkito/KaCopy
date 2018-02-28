package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IInstantiationStrategy;
import ru.siksmfp.kacopy.objenesis.Instanter;
import ru.siksmfp.kacopy.objenesis.InstanterStd;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class ObjenesisInstantiationStrategy implements IInstantiationStrategy {
    private final Instanter instanter = new InstanterStd();

    public <T> T newInstance(Class<T> c) {
        return instanter.newInstance(c);
    }

    private static ObjenesisInstantiationStrategy instance = new ObjenesisInstantiationStrategy();

    public static ObjenesisInstantiationStrategy getInstance() {
        return instance;
    }
}
