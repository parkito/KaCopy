package ru.siksmfp.kacopy.cloning;

import java.util.LinkedList;
import java.util.Map;

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
