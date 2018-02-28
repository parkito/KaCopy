package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerArrayList implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final ArrayList al = (ArrayList) t;
        final ArrayList l = new ArrayList(al.size());
        for (final Object o : al) {
            final Object cloneInternal = cloner.deepClone(o, clones);
            l.add(cloneInternal);
        }
        return l;
    }

}
