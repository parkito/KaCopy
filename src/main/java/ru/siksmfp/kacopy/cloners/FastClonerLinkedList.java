package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
public class FastClonerLinkedList implements IFastCloner {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone(Object clonedLinkedList, IDeepCloner cloner, Map<Object, Object> clones) {
        LinkedList linkedList = (LinkedList) clonedLinkedList;
        LinkedList result = new LinkedList();
        for (Object o : linkedList) {
            Object cloneInternal = cloner.deepClone(o, clones);
            result.add(cloneInternal);
        }
        return result;
    }
}
