package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerLinkedHashMap implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        LinkedHashMap<?, ?> al = (LinkedHashMap) t;
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry e : al.entrySet()) {
            Object key = cloner.deepClone(e.getKey(), clones);
            Object value = cloner.deepClone(e.getValue(), clones);

            result.put(key, value);
        }
        return result;
    }
}
