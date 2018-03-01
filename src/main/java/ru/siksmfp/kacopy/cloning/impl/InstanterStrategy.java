package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IInstantiationStrategy;
import ru.siksmfp.kacopy.instanter.api.Instanter;
import ru.siksmfp.kacopy.instanter.types.InstanterStd;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class InstanterStrategy implements IInstantiationStrategy {
    private final Instanter instanter = new InstanterStd();

    public <T> T newInstance(Class<T> c) {
        return instanter.newInstance(c);
    }

    private static InstanterStrategy instance = new InstanterStrategy();

    public static InstanterStrategy getInstance() {
        return instance;
    }
}
