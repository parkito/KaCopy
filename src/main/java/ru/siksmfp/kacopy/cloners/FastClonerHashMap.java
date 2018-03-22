package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
public class FastClonerHashMap implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        HashMap<Object, Object> m = (HashMap) t;
        HashMap result = new HashMap();
        for (Map.Entry e : m.entrySet()) {
            Object key = cloner.deepClone(e.getKey(), clones);
            Object value = cloner.deepClone(e.getValue(), clones);

            result.put(key, value);
        }
        return result;
    }
}
