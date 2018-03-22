package ru.siksmfp.kacopy.exception;

/**
 * Exception thrown by Instanter. It wraps any instantiation exceptions. Note that this exception is
 * runtime to prevent having to catch it.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 */
public class InstanterException extends RuntimeException {
    /**
     * @param msg Error message
     */
    public InstanterException(String msg) {
        super(msg);
    }

    /**
     * @param cause Wrapped exception. The message will be the one of the cause.
     */
    public InstanterException(Throwable cause) {
        super(cause);
    }

    /**
     * @param msg   Error message
     * @param cause Wrapped exception
     */
    public InstanterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
