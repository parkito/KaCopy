package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloners.CopierInternalProperties;
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
    private CopierInternalProperties properties;
    private IDeepCloner deepCloner = new IDeepCloner() {
        public <T> T deepClone(T object, Map<Object, Object> clones) {
            try {
                return cloneInternal(object, clones);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    };

    public CopierSettings settings;

    public KaCopier() {
        properties = new CopierInternalProperties();
        settings = new CopierSettings(properties);
    }

    public <T> T deepCopy(T object) {
        if (object == null) return null;
        Map<Object, Object> clones = new IdentityHashMap<Object, Object>(16);
        try {
            return cloneInternal(object, clones);
        } catch (IllegalAccessException ex) {
            throw new CloningException("Error during cloning of " + object, ex);
        }
    }

    public <T> T shallowCopy(T object) {
        if (object == null) return null;
        try {
            return cloneInternal(object, null);
        } catch (IllegalAccessException ex) {
            throw new CloningException("Error during cloning of " + object, ex);
        }
    }

    /**
     * Decides if a class is to be considered immutable or not
     *
     * @param clz the class under check
     * @return true if the clz is considered immutable
     */
    private boolean isImmutable(Class<?> clz) {
        Boolean isIm = properties.getImmutableClassesCash().get(clz);
        if (isIm != null) return isIm;

        Class<?> immutableAnnotation = Immutable.class;
        for (Annotation annotation : clz.getDeclaredAnnotations()) {
            if (annotation.annotationType() == immutableAnnotation) {
                properties.addImmutableClasseToCash(clz, Boolean.TRUE);
                return true;
            }
        }
        Class<?> c = clz.getSuperclass();
        while (c != null && c != Object.class) {
            for (Annotation annotation : c.getDeclaredAnnotations()) {
                if (annotation.annotationType() == Immutable.class) {
                    Immutable im = (Immutable) annotation;
                    if (im.subClass()) {
                        properties.getImmutableClassesCash().put(clz, Boolean.TRUE);
                        return true;
                    }
                }
            }
            c = c.getSuperclass();
        }
        properties.addImmutableClasseToCash(clz, Boolean.FALSE);
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneInternal(T o, Map<Object, Object> clones) throws IllegalAccessException {
        if (o == null) return null;
        if (o == this) return null; // We don't need to clone cloner
        if (o instanceof Enum) return o;
        Class<T> clz = (Class<T>) o.getClass();
        if (properties.getIgnoredClasses().contains(clz)) return o;
        if (isImmutable(clz)) return o;
        Object clonedPreviously = clones != null ? clones.get(o) : null;
        if (clonedPreviously != null) return (T) clonedPreviously;

        Object fastClone = fastClone(o, clones);
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
        T newInstance = properties.getInstanter().newInstance(clz);
        if (clones != null) {
            clones.put(o, newInstance);
        }
        List<Field> fields = getFieldsForClass(clz);
        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                if (!(properties.isNullTransient() && Modifier.isTransient(modifiers))) {
                    Object fieldObject = field.get(o);
                    boolean shouldClone = (properties.isCloneSynthetics() || !field.isSynthetic()) && (properties.isCloneAnonymousParent() || !isAnonymousParent(field));
                    Object fieldObjectClone = clones != null ? (shouldClone ? cloneInternal(fieldObject, clones) : fieldObject) : fieldObject;
                    field.set(newInstance, fieldObjectClone);
                }
            }
        }
        return newInstance;
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneArray(T o, Map<Object, Object> clones) throws IllegalAccessException {
        Class<T> clz = (Class<T>) o.getClass();
        int length = Array.getLength(o);
        T newInstance = (T) Array.newInstance(clz.getComponentType(), length);
        if (clones != null) {
            clones.put(o, newInstance);
        }
        if (clz.getComponentType().isPrimitive() || isImmutable(clz.getComponentType())) {
            System.arraycopy(o, 0, newInstance, 0, length);
        } else {
            for (int i = 0; i < length; i++) {
                Object v = Array.get(o, i);
                Object clone = clones != null ? cloneInternal(v, clones) : v;
                Array.set(newInstance, i, clone);
            }
        }
        return newInstance;
    }

    private boolean isAnonymousParent(Field field) {
        return "this$0".equals(field.getName());
    }

    private List<Field> getFieldsForClass(Class<?> clazz) {
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

    private Object fastClone(Object o, Map<Object, Object> clones) throws IllegalAccessException {
        Class<? extends Object> c = o.getClass();
        IFastCloner fastCloner = properties.getFastCloners().get(c);
        if (fastCloner != null) {
            return fastCloner.clone(o, deepCloner, clones);
        }
        return null;
    }
}
