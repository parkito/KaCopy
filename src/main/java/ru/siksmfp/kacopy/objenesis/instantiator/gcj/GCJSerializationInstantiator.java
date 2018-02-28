package ru.siksmfp.kacopy.objenesis.instantiator.gcj;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.SerializationInstantiatorHelper;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;

/**
 * Instantiates a class by making a call to internal GCJ private methods. It is only supposed to
 * work on GCJ JVMs. This instantiator will create classes in a way compatible with serialization,
 * calling the first non-serializable superclass' no-arg constructor.
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 * @see org.objenesis.instantiator.ObjectInstantiator
 */
@Instantiator(Typology.SERIALIZATION)
public class GCJSerializationInstantiator<T> extends GCJInstantiatorBase<T> {
    private Class<? super T> superType;

    public GCJSerializationInstantiator(Class<T> type) {
        super(type);
        this.superType = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
    }

    @Override
    public T newInstance() {
        try {
            return type.cast(newObjectMethod.invoke(dummyStream, type, superType));
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }

}
