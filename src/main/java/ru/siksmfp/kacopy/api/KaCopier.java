package ru.siksmfp.kacopy.api;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public interface KaCopier {

    <T> T deepCopy(T object);

    <T> T shallowCopy(T object);
}
