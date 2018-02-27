package ru.siksmfp.kacopy.objenesis.instantiator.basic;

import ru.siksmfp.kacopy.objenesis.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

/**
 * The instantiator that always throws an exception. Mainly used for tests
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
@Instantiator(Typology.NOT_COMPLIANT)
public class FailingInstantiator<T> implements ObjectInstantiator<T> {

    public FailingInstantiator(Class<T> type) {
    }

    /**
     * @return Always throwing an exception
     */
    public T newInstance() {
        throw new ObjenesisException("Always failing");
    }
}
