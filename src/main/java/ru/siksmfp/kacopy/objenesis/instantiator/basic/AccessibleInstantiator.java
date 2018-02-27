package ru.siksmfp.kacopy.objenesis.instantiator.basic;

import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

/**
 * Instantiates a class by grabbing the no-args constructor, making it accessible and then calling
 * Constructor.newInstance(). Although this still requires no-arg constructors, it can call
 * non-public constructors (if the security manager allows it).
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
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
