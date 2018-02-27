package ru.siksmfp.kacopy.cloning;

import java.util.Map;

/**
 * allows a custom cloner to be created for a specific class.
 * (it has to be registered with Cloner)
 */
public interface IFastCloner {
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones);
}
