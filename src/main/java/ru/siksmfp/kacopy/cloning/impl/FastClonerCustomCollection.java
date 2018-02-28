package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;

import java.util.Collection;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class FastClonerCustomCollection<T extends Collection> implements IFastCloner {
    public abstract T getInstance(T o);

    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final T c = getInstance((T) t);
        final T l = (T) t;
        for (final Object o : l) {
            final Object clone = cloner.deepClone(o, clones);
            c.add(clone);
        }
        return c;
    }
}
