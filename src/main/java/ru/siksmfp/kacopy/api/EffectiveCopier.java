package ru.siksmfp.kacopy.api;

import ru.siksmfp.kacopy.cloning.api.ICloningStrategy;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;
import ru.siksmfp.kacopy.cloning.impl.FastClonerArrayList;
import ru.siksmfp.kacopy.cloning.impl.FastClonerCalendar;
import ru.siksmfp.kacopy.cloning.impl.FastClonerConcurrentHashMap;
import ru.siksmfp.kacopy.cloning.impl.FastClonerHashMap;
import ru.siksmfp.kacopy.cloning.impl.FastClonerHashSet;
import ru.siksmfp.kacopy.cloning.impl.FastClonerLinkedHashMap;
import ru.siksmfp.kacopy.cloning.impl.FastClonerLinkedList;
import ru.siksmfp.kacopy.cloning.impl.FastClonerTreeMap;

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
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author Artem Karnov @date 3/2/2018.
 * @email artem.karnov@t-systems.com
 */
public class EffectiveCopier implements KaCopier {
    private final Set<Class<?>> ignored = new HashSet<>();
    private final Set<Class<?>> ignoredInstanceOf = new HashSet<>();
    private final Set<Class<?>> nullInstead = new HashSet<>();
    private final Map<Class<?>, IFastCloner> fastCloners = new HashMap<>();
    private final Map<Object, Boolean> ignoredInstances = new IdentityHashMap<>();
    private final ConcurrentHashMap<Class<?>, List<Field>> fieldsCache = new ConcurrentHashMap<>();
    private final List<ICloningStrategy> cloningStrategies = new LinkedList<>();

    private void init() {
        //register known Jdk immutable classes
        ignored.addAll(Arrays.asList(
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

        registerStaticFields(TreeSet.class, HashSet.class, HashMap.class, TreeMap.class);

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

    /**
     * Registers a std set of fast cloners.
     */
    private void registerFastCloners() {

    }

    /**
     * Registers an immutable class. Immutable classes are not cloned.
     *
     * @param c the immutable class
     */
    public void registerImmutable(final Class<?>... c) {
        ignored.addAll(Arrays.asList(c));
    }

    /**
     * Registers all static fields of these classes. Those static fields won't be cloned when an instance
     * of the class is cloned.
     * <p>
     * This is useful i.e. when a static field object is added into maps or sets. At that point, there is no
     * way for the cloner to know that it was static except if it is registered.
     *
     * @param classes array of classes
     */
    public void registerStaticFields(final Class<?>... classes) {
        for (final Class<?> c : classes) {
            final List<Field> fields = allFields(c);
            for (final Field field : fields) {
                final int mods = field.getModifiers();
                if (Modifier.isStatic(mods) && !field.getType().isPrimitive()) {
                    registerConstant(c, field.getName());
                }
            }
        }
    }

    /**
     * reflection utils, override this to choose which fields to clone
     */
    private List<Field> allFields(final Class<?> c) {
        List<Field> l = fieldsCache.get(c);
        if (l == null) {
            l = new LinkedList<Field>();
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

    private void registerConstant(final Class<?> c, final String privateFieldName) {
        try {
            List<Field> fields = allFields(c);
            for (Field field : fields) {
                if (field.getName().equals(privateFieldName)) {
                    field.setAccessible(true);
                    final Object v = field.get(null);
                    ignoredInstances.put(v, true);
                    return;
                }
            }
            throw new RuntimeException("No such field : " + privateFieldName);
        } catch (final SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deepCopy(T object) {
        return null;
    }

    @Override
    public <T> T shallowCopy(T object) {
        return null;
    }
}
