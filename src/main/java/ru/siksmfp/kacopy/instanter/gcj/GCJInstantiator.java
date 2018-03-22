package ru.siksmfp.kacopy.instanter.gcj;

import ru.siksmfp.kacopy.exception.InstanterException;
import ru.siksmfp.kacopy.instanter.annotations.Instantiator;
import ru.siksmfp.kacopy.instanter.annotations.Typology;

import java.lang.reflect.InvocationTargetException;

/**
 * Instantiates a class by making a call to internal GCJ private methods. It is only supposed to
 * work on GCJ JVMs. This instantiator will not call any constructors.
 *
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
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
        } catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanterException(e);
        }
    }
}
