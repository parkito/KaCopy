package ru.siksmfp.kacopy.api;

import java.util.Map;

/**
 * Used by fast cloners to deep clone objects
 *
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public interface IDeepCloner {
    /**
     * deep clones o
     *
     * @param o      the object to be deep cloned
     * @param clones pass on the same map from IFastCloner
     * @param <T>    the type of o
     * @return a clone of o
     */
    <T> T deepClone(final T o, final Map<Object, Object> clones);
}
