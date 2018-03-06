package ru.siksmfp.kacopy;

import ru.siksmfp.kacopy.api.EffectiveCopier;
import ru.siksmfp.kacopy.api.SimpleCopier;

import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class Main {
    public static void main(String[] args) {
        SimpleCopier simpleCopier = new SimpleCopier();
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> integerListClone = simpleCopier.deepCopy(integerList);
    }
}
