package ru.siksmfp.kacopy.instanter.sun;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;
import ru.siksmfp.kacopy.instanter.util.UnsafeUtils;
import sun.misc.Unsafe;

/**
 * Instantiates an object, WITHOUT calling it's constructor, using
 * {@code sun.misc.Unsafe.allocateInstance()}. Unsafe and its methods are implemented by most
 * modern JVMs.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see ObjectInstantiator
 */
@SuppressWarnings("restriction")
@Instantiator(Typology.STANDARD)
public class UnsafeFactoryInstantiator<T> implements ObjectInstantiator<T> {

    private final Unsafe unsafe;
    private final Class<T> type;

    public UnsafeFactoryInstantiator(Class<T> type) {
        this.unsafe = UnsafeUtils.getUnsafe(); // retrieve it to fail right away at instantiator creation if not there
        this.type = type;
    }

    public T newInstance() {
        try {
            return type.cast(unsafe.allocateInstance(type));
        } catch (InstantiationException e) {
            throw new InstanterException(e);
        }
    }
}
