package ru.siksmfp.kacopy.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.siksmfp.kacopy.cloning.cloner.SimpleCloneMaker;
import ru.siksmfp.kacopy.dummies.ImmutableDummy;
import ru.siksmfp.kacopy.dummies.MutableDummy;

import java.util.*;

/**
 * @author Artem Karnov @date 3/6/2018.
 * @email artem.karnov@t-systems.com
 */
class KaCopierTest {

    private KaCopier kaCopier;

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
    public void setUp() {
        kaCopier = new KaCopier();

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

    @Test
    public void listClone() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> integerListClone = kaCopier.deepCopy(integerList);

        Assertions.assertEquals(integerListClone, integerList);
    }

    @Test
    public void mapClone() {
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

    @Test
    public void primitiveClone() {
        double value = 4.0;
        double valueClone = kaCopier.deepCopy(value);

        Assertions.assertEquals(value, valueClone, 0.001);
    }

    @Test
    public void mutableObjectClone() {
        MutableDummy dummy = new MutableDummy(map, collection, intVal1, stringVal1);

        MutableDummy dummyClone = kaCopier.deepCopy(dummy);

        dummy.setMap(null);

        Assertions.assertEquals(dummyClone.getCollection(), dummy.getCollection());
        Assertions.assertEquals(dummyClone.getIntValue(), dummy.getIntValue());
        Assertions.assertEquals(dummyClone.getStringValue(), dummy.getStringValue());
        Assertions.assertNotEquals(dummyClone.getMap(), dummy.getMap());
    }

    @Test
    public void primitiveArrayClone() {
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

    @Test
    public void primitiveObjectClone() {

    }

    @Test
    public void immutableClone() {

    }

    @Test
    public void immutableInheritanceClone() {

    }

    @Test
    public void giantArrayClone() {
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

    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        ImmutableDummy immutableDummy = new ImmutableDummy(map, collection, intVal1, stringVal1);
        SimpleCloneMaker simpleCopier = new SimpleCloneMaker();
        ImmutableDummy immutableDummyClone = simpleCopier.copy(ImmutableDummy.class, immutableDummy);

        map.put(1, "4");

        Assertions.assertNotEquals(immutableDummyClone.getMap(), immutableDummy.getMap());
    }


    @Test
    public void test1() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        SimpleCloneMaker simpleCopier = new SimpleCloneMaker();
        Integer a = 5;

        Integer copy = simpleCopier.copy(Integer.class, a);
        System.out.println(copy);

    }

}