package ru.siksmfp.kacopy.instanter.android;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

/**
 * Instantiator for Android API level 10 and lover which creates objects without driving their
 * constructors, using internal methods on the Dalvik implementation of
 * {@link ObjectInputStream}.
 *
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
@Instantiator(Typology.STANDARD)
public class Android10Instantiator<T> implements ObjectInstantiator<T> {
    private final Class<T> type;
    private final Method newStaticMethod;

    public Android10Instantiator(Class<T> type) {
        this.type = type;
        newStaticMethod = getNewStaticMethod();
    }

    public T newInstance() {
        try {
            return type.cast(newStaticMethod.invoke(null, type, Object.class));
        } catch (Exception e) {
            throw new InstanterException(e);
        }
    }

    private static Method getNewStaticMethod() {
        try {
            Method newStaticMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
            newStaticMethod.setAccessible(true);
            return newStaticMethod;
        } catch (RuntimeException | NoSuchMethodException e) {
            throw new InstanterException(e);
        }
    }

}
