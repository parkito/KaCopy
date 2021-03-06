package ru.siksmfp.kacopy.instanter.perc;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

/**
 * Instantiates a class by making a call to internal Perc private methods. It is only supposed to
 * work on Perc JVMs. This instantiator will not call any constructors. The code was provided by
 * Aonix Perc support team.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
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
            throw new InstanterException(e);
        } catch (NoSuchMethodException e) {
            throw new InstanterException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        try {
            return (T) newInstanceMethod.invoke(null, typeArgs);
        } catch (Exception e) {
            throw new InstanterException(e);
        }
    }

}
