package ru.siksmfp.kacopy.instanter.basic;

import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;

/**
 * Instantiates a class by grabbing the no-args constructor, making it accessible and then calling
 * Constructor.newInstance(). Although this still requires no-arg constructors, it can call
 * non-public constructors (if the security manager allows it).
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 */
@Instantiator(Typology.NOT_COMPLIANT)
public class AccessibleInstantiator<T> extends ConstructorInstantiator<T> {

    public AccessibleInstantiator(Class<T> type) {
        super(type);
        if (constructor != null) {
            constructor.setAccessible(true);
        }
    }
}
