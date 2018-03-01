package ru.parkito.kacopy.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.strategy.PlatformDescription;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Artem Karnov @date 3/2/2018.
 * @email artem.karnov@t-systems.com
 */
public class PlatformDescriptionTest {

    @Test
    public void isJvmName() {
        PlatformDescription.isThisJVM(PlatformDescription.HOTSPOT);
    }

    @Test
    public void test() {
        if (!PlatformDescription.isThisJVM(PlatformDescription.DALVIK)) {
            assertEquals(0, PlatformDescription.ANDROID_VERSION);
        }
    }

    @Test()
    public void testAndroidVersion() throws Exception {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            Method m = PlatformDescription.class.getDeclaredMethod("getAndroidVersion0");
            m.setAccessible(true);
            int actual = (Integer) m.invoke(null);
            assertEquals(42, actual);
        });
    }
}
