package ru.siksmfp.kacopy.cloning;

import java.lang.reflect.Field;

public interface IDumpCloned {

    void startCloning(Class<?> clz);

    void cloning(Field field, Class<?> clz);
}
