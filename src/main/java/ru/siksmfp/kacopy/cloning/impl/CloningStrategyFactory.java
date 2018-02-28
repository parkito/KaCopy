package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.ICloningStrategy;

import java.lang.reflect.Field;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class CloningStrategyFactory {
    public static ICloningStrategy annotatedField(final Class annotationClass, final ICloningStrategy.Strategy strategy) {
        return new ICloningStrategy() {
            public Strategy strategyFor(Object toBeCloned, Field field) {
                if (toBeCloned == null) return Strategy.IGNORE;
                if (field.getDeclaredAnnotation(annotationClass) != null) return strategy;
                return Strategy.IGNORE;
            }
        };
    }
}
