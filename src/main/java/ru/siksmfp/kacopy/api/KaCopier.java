package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;
import ru.siksmfp.kacopy.cloning.api.IFreezable;
import ru.siksmfp.kacopy.cloning.api.Immutable;
import ru.siksmfp.kacopy.cloning.impl.*;
import ru.siksmfp.kacopy.exception.CloningException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Artem Karnov @date 3/2/2018.
 * @email artem.karnov@t-systems.com
 */
public class KaCopier {
    private CopierProperties properties;

    public KaCopier() {
        properties = new CopierProperties();
    }

    public boolean isNullTransient() {
        return properties.isNullTransient();
    }

    /**
     * This makes the cloner to set a transient field to null upon cloning.
     * <p>
     * NOTE: primitive types can't be nulled. Their value will be set to default, i.e. 0 for int
     *
     * @param nullTransient true for transient fields to be nulled
     */
    public void setNullTransient(boolean nullTransient) {
        properties.setNullTransient(nullTransient);
    }

    public void setCloneSynthetics(boolean cloneSynthetics) {
        properties.setCloneSynthetics(cloneSynthetics);
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
        fastCloners.put(c, fastCloner);
    }

    public void unregisterFastCloner(final Class<?> c) {
        fastCloners.remove(c);
    }

    /**
     * if false, anonymous classes parent class won't be cloned. Default is true
     */
    public void cloneAnonymousParent(boolean cloneAnonymousParent) {
        properties.shouldCloneAnonymousParent(cloneAnonymousParent);
    }

    public boolean isCloneAnonymousParent() {
        return cloneAnonymousParent;
    }

    public <T> T deepCopy(T object) {
        if (object == null) return null;
        final Map<Object, Object> clones = new IdentityHashMap<Object, Object>(16);
        try {
            return cloneInternal(object, clones);
        } catch (final IllegalAccessException ex) {
            throw new CloningException("error during cloning of " + object, ex);
        }
    }

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
        Class<T> clz = (Class<T>) o.getClass();
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
        T newInstance = instanter.newInstance(clz);
        if (clones != null) {
            clones.put(o, newInstance);
        }
        final List<Field> fields = getFieldsForClass(clz);
        for (final Field field : fields) {
            field.setAccessible(true);
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

    private List<Field> getFieldsForClass(final Class<?> clazz) {
        List<Field> fieldsForClass = properties.getFieldsCache().get(clazz);
        if (fieldsForClass == null) {
            fieldsForClass = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            Collections.addAll(fieldsForClass, fields);
            Class<?> superClass = clazz;
            while ((superClass = superClass.getSuperclass()) != Object.class && superClass != null) {
                Collections.addAll(fieldsForClass, superClass.getDeclaredFields());
            }
            properties.getFieldsCache().putIfAbsent(clazz, fieldsForClass);
        }
        return fieldsForClass;
    }

    private IDeepCloner deepCloner = new IDeepCloner() {
        public <T> T deepClone(T object, Map<Object, Object> clones) {
            try {
                return cloneInternal(object, clones);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    };

    private Object fastClone(final Object o, final Map<Object, Object> clones) throws IllegalAccessException {
        final Class<? extends Object> c = o.getClass();
        final IFastCloner fastCloner = properties.getFastCloners().get(c);
        if (fastCloner != null) {
            return fastCloner.clone(o, deepCloner, clones);
        }
        return null;
    }
}
