package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloners.CopierInternalProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Artem Karnov @date 3/15/2018.
 * @email artem.karnov@t-systems.com
 */
public class CopierSettings {
    private CopierInternalProperties properties;

    private CopierSettings() {
        throw new IllegalStateException("Can't instanced copier settings");
    }

    CopierSettings(CopierInternalProperties properties) {
        this.properties = properties;
    }

    /**
     * Makes the cloner to set a transient field to null upon cloning.
     * <p>
     * NOTE: primitive types can't be nulled. Their value will be set to default, i.e. 0 for int
     *
     * @param nullTransient true for transient fields to be nulled
     */
    public void setNullTransient(boolean nullTransient) {
        properties.setNullTransient(nullTransient);
    }

    public boolean isNullTransient() {
        return properties.isNullTransient();
    }

    /**
     * Makes the cloner to clone synthetics fields
     */
    public void setCloneSynthetics(boolean cloneSynthetics) {
        properties.setCloneSynthetics(cloneSynthetics);
    }

    public boolean shallCloneSynthetics() {
        return properties.isCloneSynthetics();
    }

    /**
     * if false, anonymous classes parent class won't be cloned. Default is true
     */
    public void cloneAnonymousParent(boolean cloneAnonymousParent) {
        properties.shouldCloneAnonymousParent(cloneAnonymousParent);
    }

    public boolean shallCloneAnonymousParent() {
        return properties.isCloneAnonymousParent();
    }

    /**
     * Instances of classes that shouldn't be cloned can be registered using this method.
     *
     * @param c The class that shouldn't be cloned. That is, whenever a deep clone for
     *          an object is created and c is encountered, the object instance of c will
     *          be added to the clone.
     */
    public void doNotClone(Class<?>... c) {
        properties.addIgnoredClasses(Arrays.asList(c));
    }

    public void doNotClone(List<Class<?>> classes) {
        properties.addIgnoredClasses(classes);
    }

    public void registerFastCloner(final Class<?> c, final IFastCloner fastCloner) {
        if (properties.getFastCloners().containsKey(c)) throw new IllegalArgumentException(c + " already fast-cloned!");
        properties.getFastCloners().put(c, fastCloner);
    }

    public void unregisterFastCloner(final Class<?> c) {
        properties.getFastCloners().remove(c);
    }


    public Set<Class<?>> getIgnoredClasses() {
        return Collections.unmodifiableSet(properties.getIgnoredClasses());
    }

    public Map<Class<?>, IFastCloner> getFastCloners() {
        return Collections.unmodifiableMap(properties.getFastCloners());
    }
}
