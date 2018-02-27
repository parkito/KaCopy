package ru.siksmfp.kacopy;

import ru.siksmfp.kacopy.cloning.Cloner;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class SystemUtils {
    public static <T> T clone(T t) {
        Cloner cloner = new Cloner();
        return cloner.deepClone(t);
    }
}
