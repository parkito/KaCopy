package ru.siksmfp.kacopy.cloning;

import ru.siksmfp.kacopy.objenesis.Objenesis;
import ru.siksmfp.kacopy.objenesis.ObjenesisStd;

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
