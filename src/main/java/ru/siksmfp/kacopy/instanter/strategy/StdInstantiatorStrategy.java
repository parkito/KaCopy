package ru.siksmfp.kacopy.instanter.strategy;

import ru.siksmfp.kacopy.instanter.api.ObjectInstantiator;
import ru.siksmfp.kacopy.instanter.basic.AccessibleInstantiator;
import ru.siksmfp.kacopy.instanter.basic.ObjectInputStreamInstantiator;
import ru.siksmfp.kacopy.instanter.gcj.GCJInstantiator;
import ru.siksmfp.kacopy.instanter.perc.PercInstantiator;
import ru.siksmfp.kacopy.instanter.sun.SunReflectionFactoryInstantiator;
import ru.siksmfp.kacopy.instanter.sun.UnsafeFactoryInstantiator;

import java.io.Serializable;

/**
 * Guess the best instantiator for a given class. The instantiator will instantiate the class
 * without calling any constructor. Currently, the selection doesn't depend on the class. It relies
 * on the
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
public class StdInstantiatorStrategy extends BaseInstantiatorStrategy {

    /**
     * Return an {@link ObjectInstantiator} allowing to create instance without any constructor being
     * called.
     *
     * @param type Class to instantiate
     * @return The ObjectInstantiator for the class
     */
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {

        if (PlatformDescription.isThisJVM(PlatformDescription.HOTSPOT) || PlatformDescription.isThisJVM(PlatformDescription.OPENJDK)) {
            // Java 7 GAE was under a security manager so we use a degraded system
            if (PlatformDescription.isGoogleAppEngine() && PlatformDescription.SPECIFICATION_VERSION.equals("1.7")) {
                if (Serializable.class.isAssignableFrom(type)) {
                    return new ObjectInputStreamInstantiator<T>(type);
                }
                return new AccessibleInstantiator<T>(type);
            }
            // The UnsafeFactoryInstantiator would also work. But according to benchmarks, it is 2.5
            // times slower. So we prefer to use this one
            return new SunReflectionFactoryInstantiator<T>(type);
        } else if (PlatformDescription.isThisJVM(PlatformDescription.JROCKIT)) {
            // JRockit is compliant with HotSpot
            return new SunReflectionFactoryInstantiator<T>(type);
        } else if (PlatformDescription.isThisJVM(PlatformDescription.GNU)) {
            return new GCJInstantiator<T>(type);
        } else if (PlatformDescription.isThisJVM(PlatformDescription.PERC)) {
            return new PercInstantiator<T>(type);
        }

        // Fallback instantiator, should work with most modern JVM
        return new UnsafeFactoryInstantiator<T>(type);

    }
}
