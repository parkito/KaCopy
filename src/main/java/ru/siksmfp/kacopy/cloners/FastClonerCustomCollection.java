package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.Collection;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class FastClonerCustomCollection<T extends Collection> implements IFastCloner {
    public abstract T getInstance(T o);

    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        T c = getInstance((T) t);
        T l = (T) t;
        for (Object o : l) {
            Object clone = cloner.deepClone(o, clones);
            c.add(clone);
        }
        return c;
    }
}
