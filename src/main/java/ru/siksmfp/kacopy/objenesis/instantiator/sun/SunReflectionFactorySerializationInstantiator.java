
package ru.siksmfp.kacopy.objenesis.instantiator.sun;

import ru.siksmfp.kacopy.objenesis.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.SerializationInstantiatorHelper;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;

/**
 * Instantiates an object using internal sun.reflect.ReflectionFactory - a class only available on
 * JDK's that use Sun's 1.4 (or later) Java implementation. This instantiator will create classes in
 * a way compatible with serialization, calling the first non-serializable superclass' no-arg
 * constructor. This is the best way to instantiate an object without any side effects caused by the
 * constructor - however it is not available on every platform.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see ObjectInstantiator
 */
@Instantiator(Typology.SERIALIZATION)
public class SunReflectionFactorySerializationInstantiator<T> implements ObjectInstantiator<T> {

    private final Constructor<T> mungedConstructor;

    public SunReflectionFactorySerializationInstantiator(Class<T> type) {
        Class<? super T> nonSerializableAncestor = SerializationInstantiatorHelper
                .getNonSerializableSuperClass(type);

        Constructor<? super T> nonSerializableAncestorConstructor;
        try {
            nonSerializableAncestorConstructor = nonSerializableAncestor
                    .getDeclaredConstructor((Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(new NotSerializableException(type + " has no suitable superclass constructor"));
        }

        mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(
                type, nonSerializableAncestorConstructor);
        mungedConstructor.setAccessible(true);
    }

    public T newInstance() {
        try {
            return mungedConstructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
