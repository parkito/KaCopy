package ru.siksmfp.kacopy.objenesis;

import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;

import java.io.Serializable;

/**
 * Use Objenesis in a static way. <strong>It is strongly not recommended to use this class.</strong>
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public final class ObjenesisHelper {

    private static final Objenesis OBJENESIS_STD = new ObjenesisStd();

    private static final Objenesis OBJENESIS_SERIALIZER = new ObjenesisSerializer();

    private ObjenesisHelper() {
    }

    /**
     * Will create a new object without any constructor being called
     *
     * @param <T>   Type instantiated
     * @param clazz Class to instantiate
     * @return New instance of clazz
     */
    public static <T> T newInstance(Class<T> clazz) {
        return OBJENESIS_STD.newInstance(clazz);
    }

    /**
     * Will create an object just like it's done by ObjectInputStream.readObject (the default
     * constructor of the first non serializable class will be called)
     *
     * @param <T>   Type instantiated
     * @param clazz Class to instantiate
     * @return New instance of clazz
     */
    public static <T extends Serializable> T newSerializableInstance(Class<T> clazz) {
        return (T) OBJENESIS_SERIALIZER.newInstance(clazz);
    }

    /**
     * Will pick the best instantiator for the provided class. If you need to create a lot of
     * instances from the same class, it is way more efficient to create them from the same
     * ObjectInstantiator than calling {@link #newInstance(Class)}.
     *
     * @param <T>   Type to instantiate
     * @param clazz Class to instantiate
     * @return Instantiator dedicated to the class
     */
    public static <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz) {
        return OBJENESIS_STD.getInstantiatorOf(clazz);
    }

    /**
     * Same as {@link #getInstantiatorOf(Class)} but providing an instantiator emulating
     * ObjectInputStream.readObject behavior.
     *
     * @param <T>   Type to instantiate
     * @param clazz Class to instantiate
     * @return Instantiator dedicated to the class
     * @see #newSerializableInstance(Class)
     */
    public static <T extends Serializable> ObjectInstantiator<T> getSerializableObjectInstantiatorOf(Class<T> clazz) {
        return OBJENESIS_SERIALIZER.getInstantiatorOf(clazz);
    }
}
