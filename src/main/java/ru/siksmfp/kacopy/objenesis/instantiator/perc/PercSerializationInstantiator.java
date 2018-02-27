package ru.siksmfp.kacopy.objenesis.instantiator.perc;

import ru.siksmfp.kacopy.objenesis.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Instantiates a class by making a call to internal Perc private methods. It is only supposed to
 * work on Perc JVMs. This instantiator will create classes in a way compatible with serialization,
 * calling the first non-serializable superclass' no-arg constructor.
 * <p>
 * Based on code provided by Aonix but <b>doesn't work right now</b>
 *
 * @author Henri Tremblay
 * @see org.objenesis.instantiator.ObjectInstantiator
 */
@Instantiator(Typology.SERIALIZATION)
public class PercSerializationInstantiator<T> implements ObjectInstantiator<T> {

    private Object[] typeArgs;

    private final Method newInstanceMethod;

    public PercSerializationInstantiator(Class<T> type) {

        // Find the first unserializable parent class
        Class<? super T> unserializableType = type;

        while (Serializable.class.isAssignableFrom(unserializableType)) {
            unserializableType = unserializableType.getSuperclass();
        }

        try {
            // Get the special Perc method to call
            Class<?> percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");

            newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("noArgConstruct",
                    new Class[]{Class.class, Object.class, percMethodClass});
            newInstanceMethod.setAccessible(true);

            // Create invoke params
            Class<?> percClassClass = Class.forName("COM.newmonics.PercClassLoader.PercClass");
            Method getPercClassMethod = percClassClass.getDeclaredMethod("getPercClass", Class.class);
            Object someObject = getPercClassMethod.invoke(null, unserializableType);
            Method findMethodMethod = someObject.getClass().getDeclaredMethod("findMethod",
                    new Class[]{String.class});
            Object percMethod = findMethodMethod.invoke(someObject, "<init>()V");

            typeArgs = new Object[]{unserializableType, type, percMethod};

        } catch (ClassNotFoundException e) {
            throw new ObjenesisException(e);
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(e);
        } catch (InvocationTargetException e) {
            throw new ObjenesisException(e);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        try {
            return (T) newInstanceMethod.invoke(null, typeArgs);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        } catch (InvocationTargetException e) {
            throw new ObjenesisException(e);
        }
    }

}
