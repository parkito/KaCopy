package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerLinkedList implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final LinkedList al = (LinkedList) t;
        final LinkedList l = new LinkedList();
        for (final Object o : al) {
            final Object cloneInternal = cloner.deepClone(o, clones);
            l.add(cloneInternal);
        }
        return l;
    }
}
