package ru.siksmfp.kacopy.objenesis.strategy;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.gcj.GCJSerializationInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.perc.PercSerializationInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator;

import java.io.NotSerializableException;
import java.io.Serializable;

import static ru.siksmfp.kacopy.objenesis.strategy.PlatformDescription.GNU;
import static ru.siksmfp.kacopy.objenesis.strategy.PlatformDescription.HOTSPOT;
import static ru.siksmfp.kacopy.objenesis.strategy.PlatformDescription.JVM_NAME;
import static ru.siksmfp.kacopy.objenesis.strategy.PlatformDescription.OPENJDK;
import static ru.siksmfp.kacopy.objenesis.strategy.PlatformDescription.PERC;
import static ru.siksmfp.kacopy.objenesis.strategy.PlatformDescription.isGoogleAppEngine;

/**
 * Guess the best serializing instantiator for a given class. The returned instantiator will
 * instantiate classes like the genuine java serialization framework (the constructor of the first
 * not serializable class will be called). Currently, the selection doesn't depend on the class. It
 * relies on the
 * <ul>
 * <li>JVM version</li>
 * <li>JVM vendor</li>
 * <li>JVM vendor version</li>
 * </ul>
 * However, instantiators are stateful and so dedicated to their class.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see ObjectInstantiator
 */
public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy {

    /**
     * Return an {@link ObjectInstantiator} allowing to create instance following the java
     * serialization framework specifications.
     *
     * @param type Class to instantiate
     * @return The ObjectInstantiator for the class
     */
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        if (!Serializable.class.isAssignableFrom(type)) {
            throw new ObjenesisException(new NotSerializableException(type + " not serializable"));
        }
        if (JVM_NAME.startsWith(HOTSPOT) || PlatformDescription.isThisJVM(OPENJDK)) {
            // Java 7 GAE was under a security manager so we use a degraded system
            if (isGoogleAppEngine() && PlatformDescription.SPECIFICATION_VERSION.equals("1.7")) {
                return new ObjectInputStreamInstantiator<T>(type);
            }
            return new SunReflectionFactorySerializationInstantiator<T>(type);
        } else if (JVM_NAME.startsWith(GNU)) {
            return new GCJSerializationInstantiator<T>(type);
        } else if (JVM_NAME.startsWith(PERC)) {
            return new PercSerializationInstantiator<T>(type);
        }

        return new SunReflectionFactorySerializationInstantiator<T>(type);
    }
}
