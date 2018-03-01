package ru.siksmfp.kacopy.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import ru.siksmfp.kacopy.exception.InstanterException;

/**
 * @author Artem Karnov @date 3/2/2018.
 * @email artem.karnov@t-systems.com
 */
public class InstanterExceptionTest {

    @Test
    public final void testInstanterExceptionString() {
        Exception e = new InstanterException("test");
        assertEquals("test", e.getMessage());
    }

    @Test
    public final void testInstanterExceptionThrowable() {
        Exception cause = new RuntimeException("test");
        Exception e = new InstanterException(cause);
        assertSame(cause, e.getCause());
        assertEquals(cause.toString(), e.getMessage());

        // Check null case
        e = new InstanterException((Throwable) null);
        assertNull(e.getCause());
        assertEquals(null, e.getMessage());
    }

    @Test
    public final void testInstanterExceptionStringThrowable() {
        Exception cause = new RuntimeException("test");
        Exception e = new InstanterException("msg", cause);
        assertSame(cause, e.getCause());
        assertEquals("msg", e.getMessage());

        // Check null case
        e = new InstanterException("test", null);
        assertNull(e.getCause());
        assertEquals("test", e.getMessage());
    }
}
