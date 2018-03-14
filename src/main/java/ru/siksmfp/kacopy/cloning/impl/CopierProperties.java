package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IFastCloner;
import ru.siksmfp.kacopy.instanter.api.Instanter;
import ru.siksmfp.kacopy.instanter.types.InstanterStd;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author Artem Karnov @date 3/15/2018.
 * @email artem.karnov@t-systems.com
 */
public class CopierProperties {
    private Instanter instanter;

    private Set<Class<?>> ignoredClasses = new HashSet<>();
    private Map<Class<?>, IFastCloner> fastCloners = new HashMap<>();
    private ConcurrentHashMap<Class<?>, List<Field>> fieldsCache = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<?>, Boolean> immutableClassesCash = new ConcurrentHashMap<>();

    private boolean cloneAnonymousParent;
    private boolean isNullTransient;
    private boolean isCloneSynthetics;

    public CopierProperties() {
        instanter = new InstanterStd();

        ignoredClasses = new HashSet<>();
        fastCloners = new HashMap<>();
        fieldsCache = new ConcurrentHashMap<>();
        immutableClassesCash = new ConcurrentHashMap<>();

        cloneAnonymousParent = true;
        isNullTransient = false;
        isCloneSynthetics = true;

        //register known Jdk immutable classes
        ignoredClasses.addAll(Arrays.asList(
                String.class,
                Integer.class,
                Boolean.class,
                Class.class,
                Float.class,
                Double.class,
                Character.class,
                Byte.class,
                Short.class,
                Void.class,
                BigDecimal.class,
                BigInteger.class,
                URI.class,
                URL.class,
                UUID.class,
                Pattern.class));

        // register fast cloners
        fastCloners.put(GregorianCalendar.class, new FastClonerCalendar());
        fastCloners.put(ArrayList.class, new FastClonerArrayList());
        fastCloners.put(LinkedList.class, new FastClonerLinkedList());
        fastCloners.put(HashSet.class, new FastClonerHashSet());
        fastCloners.put(HashMap.class, new FastClonerHashMap());
        fastCloners.put(TreeMap.class, new FastClonerTreeMap());
        fastCloners.put(LinkedHashMap.class, new FastClonerLinkedHashMap());
        fastCloners.put(ConcurrentHashMap.class, new FastClonerConcurrentHashMap());
    }

    public Instanter getInstanter() {
        return instanter;
    }

    public Set<Class<?>> getIgnoredClasses() {
        return ignoredClasses;
    }

    public void addIgnoredClasses(List<Class<?>> classes) {
        ignoredClasses.addAll(classes);
    }

    public Map<Class<?>, IFastCloner> getFastCloners() {
        return fastCloners;
    }

    public void setFastCloners(Map<Class<?>, IFastCloner> fastCloners) {
        this.fastCloners = fastCloners;
    }

    public ConcurrentHashMap<Class<?>, List<Field>> getFieldsCache() {
        return fieldsCache;
    }

    public void setFieldsCache(ConcurrentHashMap<Class<?>, List<Field>> fieldsCache) {
        this.fieldsCache = fieldsCache;
    }

    public ConcurrentHashMap<Class<?>, Boolean> getImmutableClassesCash() {
        return immutableClassesCash;
    }

    public void setImmutableClassesCash(ConcurrentHashMap<Class<?>, Boolean> immutableClassesCash) {
        this.immutableClassesCash = immutableClassesCash;
    }

    public boolean isCloneAnonymousParent() {
        return cloneAnonymousParent;
    }

    public void shouldCloneAnonymousParent(boolean cloneAnonymousParent) {
        this.cloneAnonymousParent = cloneAnonymousParent;
    }

    public boolean isNullTransient() {
        return isNullTransient;
    }

    public void setNullTransient(boolean nullTransient) {
        isNullTransient = nullTransient;
    }

    public boolean isCloneSynthetics() {
        return isCloneSynthetics;
    }

    public void setCloneSynthetics(boolean cloneSynthetics) {
        isCloneSynthetics = cloneSynthetics;
    }
}
