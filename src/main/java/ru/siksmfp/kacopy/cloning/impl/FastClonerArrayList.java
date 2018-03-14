package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerArrayList implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(Object object, IDeepCloner cloner, Map<Object, Object> clones) {
        ArrayList arrayList = (ArrayList) object;
        ArrayList result = new ArrayList(arrayList.size());
        for (Object o : arrayList) {
            Object cloneInternal = cloner.deepClone(o, clones);
            result.add(cloneInternal);
        }
        return result;
    }

}
