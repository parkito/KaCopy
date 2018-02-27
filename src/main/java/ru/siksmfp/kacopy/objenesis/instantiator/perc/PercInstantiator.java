package ru.siksmfp.kacopy.objenesis.instantiator.perc;

import ru.siksmfp.kacopy.objenesis.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

/**
 * Instantiates a class by making a call to internal Perc private methods. It is only supposed to
 * work on Perc JVMs. This instantiator will not call any constructors. The code was provided by
 * Aonix Perc support team.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see org.objenesis.instantiator.ObjectInstantiator
 */
@Instantiator(Typology.STANDARD)
public class PercInstantiator<T> implements ObjectInstantiator<T> {

    private final Method newInstanceMethod;

    private final Object[] typeArgs = new Object[]{null, Boolean.FALSE};

    public PercInstantiator(Class<T> type) {

        typeArgs[0] = type;

        try {
            newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class,
                    Boolean.TYPE);
            newInstanceMethod.setAccessible(true);
        } catch (RuntimeException e) {
            throw new ObjenesisException(e);
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        try {
            return (T) newInstanceMethod.invoke(null, typeArgs);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }

}
