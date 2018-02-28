package ru.siksmfp.kacopy.cloning.cloner;

import ru.siksmfp.kacopy.objenesis.Instanter;

import java.lang.reflect.Field;
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
//            final int modifiers = field.getModifiers();
//            if (!Modifier.isStatic(modifiers)) {
            Object currentFieldValue = field.get(object);
            field.set(newInstance, currentFieldValue);
//            }
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
}
