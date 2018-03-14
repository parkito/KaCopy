package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.HashSet;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerHashSet implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        HashSet al = (HashSet) t;
        HashSet l = new HashSet();
        for (Object o : al) {
            Object cloneInternal = cloner.deepClone(o, clones);
            l.add(cloneInternal);
        }
        return l;
    }
}
