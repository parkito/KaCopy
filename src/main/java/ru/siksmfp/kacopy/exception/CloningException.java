package ru.siksmfp.kacopy.exception;

/**
 * @author Artem Karnov @date 3/5/2018.
 * artyom-karnov@yandex.ru
 */
public class CloningException extends RuntimeException {
    public CloningException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
