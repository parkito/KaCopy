package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.objenesis.Objenesis;
import ru.siksmfp.kacopy.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IClone {
    private final Objenesis objenesis = new ObjenesisStd();

    public <T> T simpleDeepClone(T o) throws IllegalAccessException {
        Class<T> clz = (Class<T>) o.getClass();
        T newInstance = objenesis.newInstance(clz);
        List<Field> fields = getAllFieldsOfClass(clz);

        for (Field field : fields) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                Object currentFieldValue = field.get(o);
                field.set(newInstance, currentFieldValue);
            }
        }

        return newInstance;
    }


    private List<Field> getAllFieldsOfClass(Class<?> c) {
        List<Field> fields = new ArrayList<>();
        while (c != Object.class && c != null) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return fields;
    }
}
