package ru.siksmfp.kacopy.cloning;

import java.util.LinkedHashMap;
import java.util.Map;

public class FastClonerLinkedHashMap implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final LinkedHashMap<?, ?> al = (LinkedHashMap) t;
        final LinkedHashMap result = new LinkedHashMap();
        for (final Map.Entry e : al.entrySet()) {
            final Object key = cloner.deepClone(e.getKey(), clones);
            final Object value = cloner.deepClone(e.getValue(), clones);

            result.put(key, value);
        }
        return result;
    }
}
