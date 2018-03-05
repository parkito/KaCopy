package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloning.api.ICloningStrategy;
import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;
import ru.siksmfp.kacopy.cloning.api.IFreezable;
import ru.siksmfp.kacopy.cloning.api.Immutable;
import ru.siksmfp.kacopy.cloning.impl.FastClonerArrayList;
import ru.siksmfp.kacopy.cloning.impl.FastClonerCalendar;
import ru.siksmfp.kacopy.cloning.impl.FastClonerConcurrentHashMap;
import ru.siksmfp.kacopy.cloning.impl.FastClonerHashMap;
import ru.siksmfp.kacopy.cloning.impl.FastClonerHashSet;
import ru.siksmfp.kacopy.cloning.impl.FastClonerLinkedHashMap;
import ru.siksmfp.kacopy.cloning.impl.FastClonerLinkedList;
import ru.siksmfp.kacopy.cloning.impl.FastClonerTreeMap;
import ru.siksmfp.kacopy.exception.CloningException;
import ru.siksmfp.kacopy.instanter.api.Instanter;
import ru.siksmfp.kacopy.instanter.types.InstanterStd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author Artem Karnov @date 3/2/2018.
 * @email artem.karnov@t-systems.com
 */
public class EffectiveCopier implements KaCopier {
    private Set<Class<?>> ignoredClasses = new HashSet<>();
    private Map<Class<?>, IFastCloner> fastCloners = new HashMap<>();
    private ConcurrentHashMap<Class<?>, List<Field>> fieldsCache = new ConcurrentHashMap<>();
    private List<ICloningStrategy> cloningStrategies = new LinkedList<>();
    private ConcurrentHashMap<Class<?>, Boolean> immutablesClassesCash = new ConcurrentHashMap<Class<?>, Boolean>();
    private Instanter instanter;
    private boolean cloneAnonymousParent = true;
    private boolean nullTransient = false;
    private boolean cloneSynthetics = true;


    private void init() {
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

        // register fast clonners
        fastCloners.put(GregorianCalendar.class, new FastClonerCalendar());
        fastCloners.put(ArrayList.class, new FastClonerArrayList());
        fastCloners.put(LinkedList.class, new FastClonerLinkedList());
        fastCloners.put(HashSet.class, new FastClonerHashSet());
        fastCloners.put(HashMap.class, new FastClonerHashMap());
        fastCloners.put(TreeMap.class, new FastClonerTreeMap());
        fastCloners.put(LinkedHashMap.class, new FastClonerLinkedHashMap());
        fastCloners.put(ConcurrentHashMap.class, new FastClonerConcurrentHashMap());
    }

    /**
     * Reflection utils, override this to choose which fields to clone
     */
    private List<Field> allFields(final Class<?> c) {
        List<Field> l = fieldsCache.get(c);
        if (l == null) {
            l = new LinkedList<>();
            final Field[] fields = c.getDeclaredFields();
            Collections.addAll(l, fields);
            Class<?> sc = c;
            while ((sc = sc.getSuperclass()) != Object.class && sc != null) {
                Collections.addAll(l, sc.getDeclaredFields());
            }
            fieldsCache.putIfAbsent(c, l);
        }
        return l;
    }

    private IDeepCloner deepCloner = new IDeepCloner() {
        public <T> T deepClone(T o, Map<Object, Object> clones) {
            try {
                return cloneInternal(o, clones);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    };

    private Object fastClone(final Object o, final Map<Object, Object> clones) throws IllegalAccessException {
        final Class<? extends Object> c = o.getClass();
        final IFastCloner fastCloner = fastCloners.get(c);
        if (fastCloner != null) return fastCloner.clone(o, deepCloner, clones);
        return null;
    }

    public EffectiveCopier() {
        instanter = new InstanterStd();
        init();
    }

    /**
     * Registers an immutable class. Immutable classes are not cloned.
     *
     * @param c the immutable class
     */
    public void registerImmutable(final Class<?>... c) {
        ignoredClasses.addAll(Arrays.asList(c));
    }

    public boolean isNullTransient() {
        return nullTransient;
    }

    /**
     * This makes the cloner to set a transient field to null upon cloning.
     * <p>
     * NOTE: primitive types can't be nulled. Their value will be set to default, i.e. 0 for int
     *
     * @param nullTransient true for transient fields to be nulled
     */
    public void setNullTransient(final boolean nullTransient) {
        this.nullTransient = nullTransient;
    }

    public void setCloneSynthetics(final boolean cloneSynthetics) {
        this.cloneSynthetics = cloneSynthetics;
    }

    /**
     * Instances of classes that shouldn't be cloned can be registered using this method.
     *
     * @param c The class that shouldn't be cloned. That is, whenever a deep clone for
     *          an object is created and c is encountered, the object instance of c will
     *          be added to the clone.
     */
    public void doNotClone(final Class<?>... c) {
        ignoredClasses.addAll(Arrays.asList(c));
    }

    public void setExtraImmutables(final Set<Class<?>> set) {
        ignoredClasses.addAll(set);
    }

    public void registerFastCloner(final Class<?> c, final IFastCloner fastCloner) {
        if (fastCloners.containsKey(c)) throw new IllegalArgumentException(c + " already fast-cloned!");
        fastCloners.put(c, fastCloner);
    }

    public void unregisterFastCloner(final Class<?> c) {
        fastCloners.remove(c);
    }

    /**
     * if false, anonymous classes parent class won't be cloned. Default is true
     */
    public void setCloneAnonymousParent(final boolean cloneAnonymousParent) {
        this.cloneAnonymousParent = cloneAnonymousParent;
    }

    public boolean isCloneAnonymousParent() {
        return cloneAnonymousParent;
    }

    @Override
    public <T> T deepCopy(T object) {
        if (object == null) return null;
        final Map<Object, Object> clones = new IdentityHashMap<Object, Object>(16);
        try {
            return cloneInternal(object, clones);
        } catch (final IllegalAccessException ex) {
            throw new CloningException("error during cloning of " + object, ex);
        }
    }

    @Override
    public <T> T shallowCopy(T object) {
        if (object == null) return null;
        try {
            return cloneInternal(object, null);
        } catch (final IllegalAccessException ex) {
            throw new CloningException("error during cloning of " + object, ex);
        }
    }

    /**
     * Override this to decide if a class is immutable. Immutable classes are not cloned.
     *
     * @param clz the class under check
     * @return true to mark clz as immutable and skip cloning it
     */
    private boolean considerImmutable(final Class<?> clz) {
        return false;
    }

    private Class<?> getImmutableAnnotation() {
        return Immutable.class;
    }

    /**
     * decides if a class is to be considered immutable or not
     *
     * @param clz the class under check
     * @return true if the clz is considered immutable
     */
    private boolean isImmutable(final Class<?> clz) {
        final Boolean isIm = immutablesClassesCash.get(clz);
        if (isIm != null) return isIm;
        if (considerImmutable(clz)) return true;

        final Class<?> immutableAnnotation = getImmutableAnnotation();
        for (final Annotation annotation : clz.getDeclaredAnnotations()) {
            if (annotation.annotationType() == immutableAnnotation) {
                immutablesClassesCash.put(clz, Boolean.TRUE);
                return true;
            }
        }
        Class<?> c = clz.getSuperclass();
        while (c != null && c != Object.class) {
            for (final Annotation annotation : c.getDeclaredAnnotations()) {
                if (annotation.annotationType() == Immutable.class) {
                    final Immutable im = (Immutable) annotation;
                    if (im.subClass()) {
                        immutablesClassesCash.put(clz, Boolean.TRUE);
                        return true;
                    }
                }
            }
            c = c.getSuperclass();
        }
        immutablesClassesCash.put(clz, Boolean.FALSE);
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneInternal(final T o, final Map<Object, Object> clones) throws IllegalAccessException {
        if (o == null) return null;
        if (o == this) return null; // We don't need to clone cloner
        if (o instanceof Enum) return o;
        final Class<T> clz = (Class<T>) o.getClass();
        if (ignoredClasses.contains(clz)) return o;
        if (isImmutable(clz)) return o;
        if (o instanceof IFreezable) {
            final IFreezable f = (IFreezable) o;
            if (f.isFrozen()) return o;
        }
        final Object clonedPreviously = clones != null ? clones.get(o) : null;
        if (clonedPreviously != null) return (T) clonedPreviously;

        final Object fastClone = fastClone(o, clones);
        if (fastClone != null) {
            if (clones != null) {
                clones.put(o, fastClone);
            }
            return (T) fastClone;
        }
        if (clz.isArray()) {
            return cloneArray(o, clones);
        }

        return cloneObject(o, clones, clz);
    }

    private <T> T cloneObject(T o, Map<Object, Object> clones, Class<T> clz) throws IllegalAccessException {
        final T newInstance = instanter.newInstance(clz);
        if (clones != null) {
            clones.put(o, newInstance);
        }
        final List<Field> fields = allFields(clz);
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                if (!(nullTransient && Modifier.isTransient(modifiers))) {
                    final Object fieldObject = field.get(o);
                    final boolean shouldClone = (cloneSynthetics || !field.isSynthetic()) && (cloneAnonymousParent || !isAnonymousParent(field));
                    final Object fieldObjectClone = clones != null ? (shouldClone ? cloneInternal(fieldObject, clones) : fieldObject) : fieldObject;
                    field.set(newInstance, fieldObjectClone);
                }
            }
        }
        return newInstance;
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneArray(T o, Map<Object, Object> clones) throws IllegalAccessException {
        final Class<T> clz = (Class<T>) o.getClass();
        final int length = Array.getLength(o);
        final T newInstance = (T) Array.newInstance(clz.getComponentType(), length);
        if (clones != null) {
            clones.put(o, newInstance);
        }
        if (clz.getComponentType().isPrimitive() || isImmutable(clz.getComponentType())) {
            System.arraycopy(o, 0, newInstance, 0, length);
        } else {
            for (int i = 0; i < length; i++) {
                final Object v = Array.get(o, i);
                final Object clone = clones != null ? cloneInternal(v, clones) : v;
                Array.set(newInstance, i, clone);
            }
        }
        return newInstance;
    }

    private boolean isAnonymousParent(final Field field) {
        return "this$0".equals(field.getName());
    }
}
