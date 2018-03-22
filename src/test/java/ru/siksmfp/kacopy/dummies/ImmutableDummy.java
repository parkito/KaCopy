package ru.siksmfp.kacopy.dummies;

import java.util.Collection;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/14/2018.
 * artyom-karnov@yandex.ru
 */
public class ImmutableDummy {
    public static final String STATIC_FIELD = "Static field";
    private Map<Integer, String> map;
    private Collection collection;
    private int intValue;
    private String stringValue;

    public ImmutableDummy(Map<Integer, String> map, Collection collection, int intValue, String stringValue) {
        this.map = map;
        this.collection = collection;
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    public Collection getCollection() {
        return collection;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
