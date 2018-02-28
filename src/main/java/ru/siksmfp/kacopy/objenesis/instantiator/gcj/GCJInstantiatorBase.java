
package ru.siksmfp.kacopy.objenesis.instantiator.gcj;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

/**
 * Base class for GCJ-based instantiators. It initializes reflection access to method
 * ObjectInputStream.newObject, as well as creating a dummy ObjectInputStream to be used as the
 * "this" argument for the method.
 *
 * @author Leonardo Mesquita
 */
public abstract class GCJInstantiatorBase<T> implements ObjectInstantiator<T> {
    static Method newObjectMethod = null;
    static ObjectInputStream dummyStream;

    private static class DummyStream extends ObjectInputStream {
        public DummyStream() throws IOException {
        }
    }

    private static void initialize() {
        if (newObjectMethod == null) {
            try {
                newObjectMethod = ObjectInputStream.class.getDeclaredMethod("newObject", new Class[]{
                        Class.class, Class.class});
                newObjectMethod.setAccessible(true);
                dummyStream = new DummyStream();
            } catch (RuntimeException e) {
                throw new ObjenesisException(e);
            } catch (NoSuchMethodException e) {
                throw new ObjenesisException(e);
            } catch (IOException e) {
                throw new ObjenesisException(e);
            }
        }
    }

    protected final Class<T> type;

    public GCJInstantiatorBase(Class<T> type) {
        this.type = type;
        initialize();
    }

    public abstract T newInstance();
}
