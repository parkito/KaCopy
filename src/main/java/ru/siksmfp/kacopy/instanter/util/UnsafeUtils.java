package ru.siksmfp.kacopy.instanter.util;

import ru.siksmfp.kacopy.exception.InstanterException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Helper class basically allowing to get access to {@code sun.misc.Unsafe}
 *
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
public final class UnsafeUtils {

    private static final Unsafe unsafe;

    static {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            throw new InstanterException(e);
        }
        f.setAccessible(true);
        try {
            unsafe = (Unsafe) f.get(null);
        } catch (IllegalAccessException e) {
            throw new InstanterException(e);
        }
    }

    private UnsafeUtils() {
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
