package ru.siksmfp.kacopy.cloning;

public interface IInstantiationStrategy {
    <T> T newInstance(final Class<T> c);
}
