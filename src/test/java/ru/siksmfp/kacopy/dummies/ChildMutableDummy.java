package ru.siksmfp.kacopy.dummies;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Artem Karnov @date 3/14/2018.
 * @email artem.karnov@t-systems.com
 */
public class ChildMutableDummy extends MutableDummy {
    private List<String> list;

    public ChildMutableDummy(Map<Integer, String> map,
                             Collection collection,
                             int intValue,
                             String stringValue,
                             List<String> list) {
        super(map, collection, intValue, stringValue);
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }
}
