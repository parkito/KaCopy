package ru.siksmfp.kacopy.cloning;

import java.util.Map;
import java.util.TreeMap;

public class FastClonerTreeMap implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final TreeMap<Object, Object> m = (TreeMap) t;
        final TreeMap result = new TreeMap(m.comparator());
        for (final Map.Entry e : m.entrySet()) {
            final Object key = cloner.deepClone(e.getKey(), clones);
            final Object value = cloner.deepClone(e.getValue(), clones);
            result.put(key, value);
        }
        return result;
    }
}
