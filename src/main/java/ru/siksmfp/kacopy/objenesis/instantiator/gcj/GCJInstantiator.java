package ru.siksmfp.kacopy.objenesis.instantiator.gcj;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

import java.lang.reflect.InvocationTargetException;

/**
 * Instantiates a class by making a call to internal GCJ private methods. It is only supposed to
 * work on GCJ JVMs. This instantiator will not call any constructors.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see org.objenesis.instantiator.ObjectInstantiator
 */
@Instantiator(Typology.STANDARD)
public class GCJInstantiator<T> extends GCJInstantiatorBase<T> {
    public GCJInstantiator(Class<T> type) {
        super(type);
    }

    @Override
    public T newInstance() {
        try {
            return type.cast(newObjectMethod.invoke(dummyStream, type, Object.class));
        } catch (RuntimeException e) {
            throw new ObjenesisException(e);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        } catch (InvocationTargetException e) {
            throw new ObjenesisException(e);
        }
    }
}
