package ru.siksmfp.kacopy;

import ru.siksmfp.kacopy.cloning.impl.Cloner;
import ru.siksmfp.kacopy.cloning.impl.IClone;

import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        IClone cloner = new IClone();
        List<Integer> cloneList = cloner.simpleDeepClone(list);
        System.out.println(list==cloneList);
    }
}
