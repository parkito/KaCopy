package ru.siksmfp.kacopy.dummies;

import java.util.Collection;
import java.util.Map;

public class MutableDummy {
    private static final String STATIC_FIELD = "Static field";
    private Map<Integer, String> map;
    private Collection collection;
    private int intValue;
    private String stringValue;

    public MutableDummy(Map<Integer, String> map, Collection collection, int intValue, String stringValue) {
        this.map = map;
        this.collection = collection;
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        this.map = map;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
