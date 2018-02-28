package ru.siksmfp.kacopy.api;

public interface KaCopier<T> {

    T deepCopy(T object);

    T shallowCopy(T object);
}
