package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloning.cloner.SimpleCloneMaker;
import ru.siksmfp.kacopy.exception.CloningException;
import ru.siksmfp.kacopy.objenesis.Instanter;
import ru.siksmfp.kacopy.objenesis.InstanterStd;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class SimpleCopier<T> {

    private SimpleCloneMaker simpleCloneMaker;
    private Instanter instanter;

    public SimpleCopier() {
        simpleCloneMaker = new SimpleCloneMaker();
        instanter = new InstanterStd();
    }

    public T deepCopy(T object) throws CloningException {
        try {
            return simpleCloneMaker.simpleDeepClone(object, instanter);
        } catch (Exception ex) {
            throw new CloningException("Cloning is failed (" + object + ")", ex);
        }
    }
}
