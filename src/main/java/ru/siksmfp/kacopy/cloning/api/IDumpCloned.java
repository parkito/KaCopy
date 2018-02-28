package ru.siksmfp.kacopy.cloning.api;

import java.lang.reflect.Field;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public interface IDumpCloned {

    void startCloning(Class<?> clz);

    void cloning(Field field, Class<?> clz);
}
