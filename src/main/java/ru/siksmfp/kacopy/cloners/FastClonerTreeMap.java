package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerTreeMap implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        TreeMap<Object, Object> m = (TreeMap) t;
        TreeMap result = new TreeMap(m.comparator());
        for (Map.Entry e : m.entrySet()) {
            Object key = cloner.deepClone(e.getKey(), clones);
            Object value = cloner.deepClone(e.getValue(), clones);
            result.put(key, value);
        }
        return result;
    }
}
