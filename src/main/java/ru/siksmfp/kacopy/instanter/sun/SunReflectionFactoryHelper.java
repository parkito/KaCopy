package ru.siksmfp.kacopy.instanter.sun;

import ru.siksmfp.kacopy.exception.InstanterException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Helper methods providing access to {@link sun.reflect.ReflectionFactory} via reflection, for use
 * by the {@link ru.siksmfp.kacopy.instanter.api.ObjectInstantiator}s that use it.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 */
@SuppressWarnings("restriction")
class SunReflectionFactoryHelper {

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> newConstructorForSerialization(Class<T> type,
                                                                    Constructor<?> constructor) {
        Class<?> reflectionFactoryClass = getReflectionFactoryClass();
        Object reflectionFactory = createReflectionFactory(reflectionFactoryClass);

        Method newConstructorForSerializationMethod = getNewConstructorForSerializationMethod(
                reflectionFactoryClass);

        try {
            return (Constructor<T>) newConstructorForSerializationMethod.invoke(reflectionFactory, type, constructor);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanterException(e);
        }
    }

    private static Class<?> getReflectionFactoryClass() {
        try {
            return Class.forName("sun.reflect.ReflectionFactory");
        } catch (ClassNotFoundException e) {
            throw new InstanterException(e);
        }
    }

    private static Object createReflectionFactory(Class<?> reflectionFactoryClass) {
        try {
            Method method = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory");
            return method.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InstanterException(e);
        }
    }

    private static Method getNewConstructorForSerializationMethod(Class<?> reflectionFactoryClass) {
        try {
            return reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", Class.class, Constructor.class);
        } catch (NoSuchMethodException e) {
            throw new InstanterException(e);
        }
    }
}
