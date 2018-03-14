package ru.siksmfp.kacopy.cloning.cloner;

import ru.siksmfp.kacopy.instanter.api.Instanter;
import ru.siksmfp.kacopy.instanter.types.InstanterStd;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://vyazelenko.com/2013/10/29/copy-object-in-java-performance-comparison/
 */
public class SimpleCloneMaker {

    public <T> T simpleDeepClone(T object, Instanter instanter) throws IllegalAccessException {
        Class<T> clz = (Class<T>) object.getClass();
        T newInstance = instanter.newInstance(clz);
        List<Field> fields = getAllFieldsOfClass(clz);

        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                Object currentFieldValue = field.get(object);
                field.set(newInstance, currentFieldValue);
            }
        }

        return newInstance;
    }

    private List<Field> getAllFieldsOfClass(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        while (cls != Object.class && cls != null) {
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            cls = cls.getSuperclass();
        }
        return fields;
    }

    private <T> Object copyObject(List<Field> fields, T newInstance, T object) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                if (field.getType().isPrimitive()) {
                    Object currentFieldValue = field.get(object);
                    field.set(newInstance, currentFieldValue);
                } else {
                    List<Field> fields1 = Arrays.asList(field.getClass().getFields());
                    field.set(newInstance, copyObject(fields1, newInstance, field.get(object)));
                }
            }
        }
        return newInstance;
    }

    public <T> T copy(Class<?> clazz, T obj) throws IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Instanter instanter = new InstanterStd();
        T newInstance;
        try {
            newInstance = instanter.newInstance((Class<T>) clazz);
        } catch (Error ex) {
            return null;
        }
        List<Field> fields = getAllFieldsOfClass(clazz);

        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                Object currentFieldValue = field.get(obj);
                if (field.getType().isPrimitive() || field.getType() == Object.class) {
                    field.set(newInstance, currentFieldValue);
                } else {
                    Object copy = copy(currentFieldValue.getClass(), currentFieldValue);
                    if (copy != null)
                        field.set(newInstance, copy);
                }
            }
        }
        return newInstance;
    }

}
