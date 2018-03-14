package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.Map;
import java.util.Set;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class FastClonerCustomMap<T extends Map> implements IFastCloner {
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        T m = (T) t;
        T result = getInstance((T) t);
        Set<Map.Entry<Object, Object>> entrySet = m.entrySet();
        for (Map.Entry e : entrySet) {
            Object key = cloner.deepClone(e.getKey(), clones);
            Object value = cloner.deepClone(e.getValue(), clones);
            result.put(key, value);
        }
        return result;
    }

    protected abstract T getInstance(T t);
}
