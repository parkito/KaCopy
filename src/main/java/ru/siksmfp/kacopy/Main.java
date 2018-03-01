package ru.siksmfp.kacopy;

import ru.siksmfp.kacopy.api.SimpleCopier;

import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        SimpleCopier<List<Integer>> cp = new SimpleCopier<>();
        List<Integer> cloneList = cp.deepCopy(list);
        System.out.println(list == cloneList);
        System.out.println(list.equals(cloneList));
    }
}
