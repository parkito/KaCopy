package ru.siksmfp.kacopy;

import ru.siksmfp.kacopy.cloning.impl.Cloner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        Cloner cloner = new Cloner();
        List<Integer> cloneList = cloner.deepClone(list);
        System.out.println(cloneList);
    }
}
