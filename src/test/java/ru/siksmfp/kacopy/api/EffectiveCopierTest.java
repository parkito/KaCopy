package ru.siksmfp.kacopy.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Karnov @date 3/6/2018.
 * @email artem.karnov@t-systems.com
 */
class EffectiveCopierTest {

    private EffectiveCopier effectiveCopier;

    @BeforeEach
    public void setUp() {
        effectiveCopier = new EffectiveCopier();

    }

    @Test
    public void listClone() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> integerListClone = effectiveCopier.deepCopy(integerList);

        Assertions.assertEquals(integerListClone, integerList);
    }

    @Test
    public void mapClone() {

    }

    @Test
    public void primitiveClone() {

    }

    @Test
    public void objectClone(){

    }

    @Test
    public void primitiveArrayClone() {

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

}