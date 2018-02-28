package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloning.cloner.SimpleCloneMaker;
import ru.siksmfp.kacopy.exception.CloningException;
import ru.siksmfp.kacopy.objenesis.Instanter;
import ru.siksmfp.kacopy.objenesis.InstanterStd;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class SimpleStaticCopier {
    private static final SimpleCloneMaker SIMPLE_CLON_MAKER = new SimpleCloneMaker();
    private static final Instanter INSTANTER = new InstanterStd();

    public <T> T deepCopy(T object) throws CloningException {
        try {
            return SIMPLE_CLON_MAKER.simpleDeepClone(object, INSTANTER);
        } catch (Exception ex) {
            throw new CloningException("Cloning is failed (" + object + ")", ex);
        }
    }
}
