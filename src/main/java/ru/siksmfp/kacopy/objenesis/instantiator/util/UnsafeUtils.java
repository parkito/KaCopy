
package ru.siksmfp.kacopy.objenesis.instantiator.util;

import ru.siksmfp.kacopy.objenesis.ObjenesisException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Helper class basically allowing to get access to {@code sun.misc.Unsafe}
 *
 * @author Henri Tremblay
 */
public final class UnsafeUtils {

    private static final Unsafe unsafe;

    static {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            throw new ObjenesisException(e);
        }
        f.setAccessible(true);
        try {
            unsafe = (Unsafe) f.get(null);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        }
    }

    private UnsafeUtils() {
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
