package ru.siksmfp.kacopy.objenesis.instantiator.basic;

import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

/**
 * The instantiator that always return a null instance
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
@Instantiator(Typology.NOT_COMPLIANT)
public class NullInstantiator<T> implements ObjectInstantiator<T> {

    public NullInstantiator(Class<T> type) {
    }

    /**
     * @return Always null
     */
    public T newInstance() {
        return null;
    }
}
