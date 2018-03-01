package ru.siksmfp.kacopy.instanter.android;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;
import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;

import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Instantiator for Android API leve 18 and higher. Same as the version 17 but the
 * <code>newInstance</code> now takes a long in parameter
 *
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
@Instantiator(Typology.STANDARD)
public class Android18Instantiator<T> implements ObjectInstantiator<T> {
    private final Class<T> type;
    private final Method newInstanceMethod;
    private final Long objectConstructorId;

    public Android18Instantiator(Class<T> type) {
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
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Long.TYPE);
            newInstanceMethod.setAccessible(true);
            return newInstanceMethod;
        } catch (NoSuchMethodException | RuntimeException e) {
            throw new InstanterException(e);
        }

    }

    private static Long findConstructorIdForJavaLangObjectConstructor() {
        try {
            Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
            newInstanceMethod.setAccessible(true);

            return (Long) newInstanceMethod.invoke(null, Object.class);
        } catch (RuntimeException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanterException(e);
        }
    }
}
