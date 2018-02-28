package ru.siksmfp.kacopy.cloning.api;

import java.util.Map;

/**
 * Allows a custom cloner to be created for a specific class.
 * (it has to be registered with Cloner)
 *
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public interface IFastCloner {
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones);
}
