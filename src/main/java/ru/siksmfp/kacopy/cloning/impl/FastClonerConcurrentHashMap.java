package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerConcurrentHashMap implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final ConcurrentHashMap<Object, Object> m = (ConcurrentHashMap) t;
        final ConcurrentHashMap result = new ConcurrentHashMap();
        for (final Map.Entry e : m.entrySet()) {
            final Object key = cloner.deepClone(e.getKey(), clones);
            final Object value = cloner.deepClone(e.getValue(), clones);

            result.put(key, value);
        }
        return result;
    }
}
