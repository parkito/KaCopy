package ru.siksmfp.kacopy.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.siksmfp.kacopy.dummies.ChildImmutableDummy;
import ru.siksmfp.kacopy.dummies.ImmutableDummy;
import ru.siksmfp.kacopy.dummies.MutableDummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Artem Karnov @date 3/6/2018.
 * @email artem.karnov@t-systems.com
 */
class CopiersTest {

    static Stream<Arguments> copiers() {
        return Stream.of(Arguments.of(new EffectiveCopier()), Arguments.of(new SimpleCopier()));
    }

    private Map<Integer, String> map;
    private Collection collection;
    private List<String> list;

    private int intVal1 = 1;
    private int intVal2 = 2;
    private int intVal3 = 3;

    private String stringVal1 = "1";
    private String stringVal2 = "2";
    private String stringVal3 = "3";

    @BeforeEach
    void setUp() {
        map = new HashMap<>();
        map.put(intVal1, stringVal1);
        map.put(intVal2, stringVal2);
        map.put(intVal3, stringVal3);

        collection = new ArrayList();
        collection.add(intVal1);
        collection.add(intVal2);
        collection.add(intVal3);

        list = new ArrayList<>();
        list.add(stringVal1);
        list.add(stringVal2);
        list.add(stringVal3);

    }

    @ParameterizedTest
    @MethodSource("copiers")
    void listClone(KaCopier kaCopier) {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> integerListClone = kaCopier.deepCopy(integerList);

        Assertions.assertEquals(integerListClone, integerList);
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void mapClone(KaCopier kaCopier) {
        Map<Integer, String> map = new HashMap<>();
        map.put(intVal1, stringVal1);
        map.put(intVal2, stringVal2);
        map.put(intVal3, stringVal3);

        Map<Integer, String> mapClone = kaCopier.deepCopy(map);

        Assertions.assertFalse(map == mapClone);
        Assertions.assertEquals(mapClone.get(intVal1), map.get(intVal1));
        Assertions.assertEquals(mapClone.get(intVal2), map.get(intVal2));
        Assertions.assertEquals(mapClone.get(intVal3), map.get(intVal3));
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void primitiveClone(KaCopier kaCopier) {
        double value = 4.0;
        double valueClone = kaCopier.deepCopy(value);

        Assertions.assertEquals(value, valueClone, 0.001);
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void mutableObjectClone(KaCopier kaCopier) {
        MutableDummy dummy = new MutableDummy(map, collection, intVal1, stringVal1);

        MutableDummy dummyClone = kaCopier.deepCopy(dummy);

        dummy.setMap(null);

        Assertions.assertEquals(dummyClone.getCollection(), dummy.getCollection());
        Assertions.assertEquals(dummyClone.getIntValue(), dummy.getIntValue());
        Assertions.assertEquals(dummyClone.getStringValue(), dummy.getStringValue());
        Assertions.assertNotEquals(dummyClone.getMap(), dummy.getMap());
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void primitiveArrayClone(KaCopier kaCopier) {
        int[] arr = new int[5];
        arr[0] = 10;
        arr[1] = 20;
        arr[2] = 30;
        arr[3] = 40;
        arr[4] = 50;

        int[] arrClone = kaCopier.deepCopy(arr);

        Assertions.assertFalse(arr == arrClone);
        for (int i = 0; i < arr.length; i++) {
            Assertions.assertEquals(arrClone[i], arr[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void immutableClone(KaCopier kaCopier) {
        ImmutableDummy immutableDummy = new ImmutableDummy(map, collection, intVal1, stringVal1);

        ImmutableDummy immutableDummyClone = kaCopier.deepCopy(immutableDummy);

        map.put(intVal1, stringVal2);

        Assertions.assertFalse(immutableDummy == immutableDummyClone);
        Assertions.assertEquals(immutableDummyClone.getCollection(), immutableDummy.getCollection());
        Assertions.assertEquals(immutableDummyClone.getIntValue(), immutableDummy.getIntValue());
        Assertions.assertEquals(immutableDummyClone.getStringValue(), immutableDummy.getStringValue());
        Assertions.assertNotEquals(immutableDummyClone.getMap(), immutableDummy.getMap());
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void immutableInheritanceClone(KaCopier kaCopier) {
        ChildImmutableDummy immutableDummyChild = new ChildImmutableDummy(map, collection, intVal1, stringVal1, list);

        ChildImmutableDummy immutableDummyChildClone = kaCopier.deepCopy(immutableDummyChild);

        Assertions.assertFalse(immutableDummyChild == immutableDummyChildClone);
        Assertions.assertEquals(immutableDummyChildClone.getMap(), immutableDummyChild.getMap());
        Assertions.assertEquals(immutableDummyChildClone.getCollection(), immutableDummyChild.getCollection());
        Assertions.assertEquals(immutableDummyChildClone.getIntValue(), immutableDummyChild.getIntValue());
        Assertions.assertEquals(immutableDummyChildClone.getStringValue(), immutableDummyChild.getStringValue());
        Assertions.assertEquals(immutableDummyChildClone.getList(), immutableDummyChild.getList());

        ImmutableDummy immutableDummyClone = (ImmutableDummy) immutableDummyChildClone;

        Assertions.assertEquals(immutableDummyClone.getMap(), immutableDummyChild.getMap());
        Assertions.assertEquals(immutableDummyClone.getCollection(), immutableDummyChild.getCollection());
        Assertions.assertEquals(immutableDummyClone.getIntValue(), immutableDummyChild.getIntValue());
        Assertions.assertEquals(immutableDummyClone.getStringValue(), immutableDummyChild.getStringValue());
    }

    @ParameterizedTest
    @MethodSource("copiers")
    void giantArrayClone(KaCopier kaCopier) {
        int maxSize = Integer.MAX_VALUE / 1000000;
        int[][] arr = new int[maxSize][maxSize];

        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                arr[i][j] = j;
            }
        }

        int[][] arrClone = kaCopier.deepCopy(arr);

        Assertions.assertFalse(arr == arrClone);
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                Assertions.assertEquals(arrClone[i][j], arr[i][j]);
            }
        }
    }
}