package ru.siksmfp.kacopy.instanter.android;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;

import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Instantiator for Android API level 11 to 17 which creates objects without driving their
 * constructors, using internal methods on the Dalvik implementation of {@link ObjectStreamClass}.
 *
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
@Instantiator(Typology.STANDARD)
public class Android17Instantiator<T> implements ObjectInstantiator<T> {
    private final Class<T> type;
    private final Method newInstanceMethod;
    private final Integer objectConstructorId;

    public Android17Instantiator(Class<T> type) {
        this.type = type;
        newInstanceMethod = getNewInstanceMethod();
        objectConstructorId = findConstructorIdForJavaLangObjectConstructor();
    }

    public T newInstance() {
        try {
            return type.cast(newInstanceMethod.invoke(null, type, objectConstructorId));
        } catch (Exception e) {
            throw new InstanterException(e);
        }
    }

    private static Method getNewInstanceMethod() {
        try {
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
            newInstanceMethod.setAccessible(true);
            return newInstanceMethod;
        } catch (RuntimeException | NoSuchMethodException e) {
            throw new InstanterException(e);
        }
    }

    private static Integer findConstructorIdForJavaLangObjectConstructor() {
        try {
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
            newInstanceMethod.setAccessible(true);

            return (Integer) newInstanceMethod.invoke(null, Object.class);
        } catch (RuntimeException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanterException(e);
        }
    }
}
