package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloners.CopierInternalProperties;
import ru.siksmfp.kacopy.exception.CloningException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
        Map<Object, Object> clones = new IdentityHashMap<>(32);
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
    private <T> T cloneInternal(T object, Map<Object, Object> clonedFields) throws IllegalAccessException {
        if (object == null || object == this) {
            return null;
        }
        if (object instanceof Enum) {
            return object;
        }

        Class<T> clz = (Class<T>) object.getClass();

        if (properties.getIgnoredClasses().contains(clz) || isImmutable(clz)) {
            return object;
        }

        Object clonedPreviously = clonedFields != null ? clonedFields.get(object) : null;
        if (clonedPreviously != null) return (T) clonedPreviously;

        Object fastClonedObject = fastClone(object, clonedFields);
        if (fastClonedObject != null) {
            if (clonedFields != null) {
                clonedFields.put(object, fastClonedObject);
            }
            return (T) fastClonedObject;
        }
        if (clz.isArray()) {
            return cloneArray(object, clonedFields);
        }

        return cloneObject(object, clonedFields, clz);
    }

    private <T> T cloneObject(T object, Map<Object, Object> clonedFields, Class<T> clz) throws IllegalAccessException {
        T newInstance = properties.getInstanter().newInstance(clz);
        if (clonedFields != null) {
            clonedFields.put(object, newInstance);
        }
        List<Field> fields = getFieldsForClass(clz);
        for (Field field : fields) {
            field.setAccessible(true);
            int modifier = field.getModifiers();
            if (!Modifier.isStatic(modifier)) {
                if (!(properties.isNullTransient() && Modifier.isTransient(modifier))) {
                    Object fieldObject = field.get(object);
                    boolean shouldClone = (properties.isCloneSynthetics() || !field.isSynthetic())
                            && (properties.isCloneAnonymousParent() || !isAnonymousParent(field));

                    Object fieldObjectClone = clonedFields != null
                            ? (shouldClone ? cloneInternal(fieldObject, clonedFields) : fieldObject)
                            : fieldObject;
                    field.set(newInstance, fieldObjectClone);
                }
            }
        }
        return newInstance;
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneArray(T object, Map<Object, Object> clones) throws IllegalAccessException {
        Class<T> clz = (Class<T>) object.getClass();
        int length = Array.getLength(object);
        T newArray = (T) Array.newInstance(clz.getComponentType(), length);
        if (clones != null) {
            clones.put(object, newArray);
        }
        if (clz.getComponentType().isPrimitive() || isImmutable(clz.getComponentType())) {
            System.arraycopy(object, 0, newArray, 0, length);
        } else {
            for (int i = 0; i < length; i++) {
                Object v = Array.get(object, i);
                Object clone = clones != null ? cloneInternal(v, clones) : v;
                Array.set(newArray, i, clone);
            }
        }
        return newArray;
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
