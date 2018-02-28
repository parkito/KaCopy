package ru.siksmfp.kacopy.cloning.api;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public interface IInstantiationStrategy {
    <T> T newInstance(final Class<T> c);
}
