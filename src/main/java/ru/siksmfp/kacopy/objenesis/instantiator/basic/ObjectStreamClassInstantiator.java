package ru.siksmfp.kacopy.objenesis.instantiator.basic;

import ru.siksmfp.kacopy.objenesis.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

/**
 * Instantiates a class by using reflection to make a call to private method
 * ObjectStreamClass.newInstance, present in many JVM implementations. This instantiator will create
 * classes in a way compatible with serialization, calling the first non-serializable superclass'
 * no-arg constructor.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see ObjectInstantiator
 * @see java.io.Serializable
 */
@Instantiator(Typology.SERIALIZATION)
public class ObjectStreamClassInstantiator<T> implements ObjectInstantiator<T> {

    private static Method newInstanceMethod;

    private static void initialize() {
        if (newInstanceMethod == null) {
            try {
                newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance");
                newInstanceMethod.setAccessible(true);
            } catch (RuntimeException e) {
                throw new ObjenesisException(e);
            } catch (NoSuchMethodException e) {
                throw new ObjenesisException(e);
            }
        }
    }

    private final ObjectStreamClass objStreamClass;

    public ObjectStreamClassInstantiator(Class<T> type) {
        initialize();
        objStreamClass = ObjectStreamClass.lookup(type);
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {

        try {
            return (T) newInstanceMethod.invoke(objStreamClass);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }

    }

}
