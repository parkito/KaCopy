package ru.siksmfp.kacopy.objenesis.instantiator;

import java.io.Serializable;

/**
 * Helper for common serialization-compatible instantiation functions
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class SerializationInstantiatorHelper {

    /**
     * Returns the first non-serializable superclass of a given class. According to Java Object
     * Serialization Specification, objects read from a stream are initialized by calling an
     * accessible no-arg constructor from the first non-serializable superclass in the object's
     * hierarchy, allowing the state of non-serializable fields to be correctly initialized.
     *
     * @param <T>  Type to instantiate
     * @param type Serializable class for which the first non-serializable superclass is to be found
     * @return The first non-serializable superclass of 'type'.
     * @see Serializable
     */
    public static <T> Class<? super T> getNonSerializableSuperClass(Class<T> type) {
        Class<? super T> result = type;
        while (Serializable.class.isAssignableFrom(result)) {
            result = result.getSuperclass();
            if (result == null) {
                throw new Error("Bad class hierarchy: No non-serializable parents");
            }
        }
        return result;

    }
}
