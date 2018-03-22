package ru.siksmfp.kacopy.api;

import java.util.Map;

/**
 * Allows a custom cloner to be created for a specific class.
 * (it has to be registered with Cloner)
 *
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
public interface IFastCloner {
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones);
}
