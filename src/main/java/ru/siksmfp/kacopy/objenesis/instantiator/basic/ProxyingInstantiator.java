package ru.siksmfp.kacopy.objenesis.instantiator.basic;

import ru.siksmfp.kacopy.exception.ObjenesisException;
import ru.siksmfp.kacopy.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Instantiator;
import ru.siksmfp.kacopy.objenesis.instantiator.annotations.Typology;
import ru.siksmfp.kacopy.objenesis.instantiator.util.ClassDefinitionUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This instantiator creates a class by dynamically extending it. It will skip the call to the parent constructor
 * in the bytecode. So that the constructor is indeed not called but you however instantiate a child class, not
 * the actual class. The class loader will normally throw a {@code VerifyError} is you do that. However, using
 * {@code -Xverify:none} should make it work
 *
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
@Instantiator(Typology.STANDARD)
public class ProxyingInstantiator<T> implements ObjectInstantiator<T> {

    private static final int INDEX_CLASS_THIS = 1;
    private static final int INDEX_CLASS_SUPERCLASS = 2;
    private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
    private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
    private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
    private static final int INDEX_UTF8_CLASS = 7;
    private static final int INDEX_UTF8_SUPERCLASS = 8;

    private static int CONSTANT_POOL_COUNT = 9;

    private static final byte[] CODE = {ClassDefinitionUtils.OPS_aload_0, ClassDefinitionUtils.OPS_return};
    private static final int CODE_ATTRIBUTE_LENGTH = 12 + CODE.length;

    private static final String SUFFIX = "$$$Instanter";

    private static final String CONSTRUCTOR_NAME = "<init>";
    private static final String CONSTRUCTOR_DESC = "()V";

    private final Class<?> newType;

    public ProxyingInstantiator(Class<T> type) {

        byte[] classBytes = writeExtendingClass(type, SUFFIX);

        try {
            newType = ClassDefinitionUtils.defineClass(type.getName() + SUFFIX, classBytes, type.getClassLoader());
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        try {
            return (T) newType.newInstance();
        } catch (InstantiationException e) {
            throw new ObjenesisException(e);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        }
    }

    /**
     * Will generate the bytes for a class extending the type passed in parameter. This class will
     * only have an empty default constructor
     *
     * @param type   type to extend
     * @param suffix the suffix appended to the class name to create the next extending class name
     * @return the byte for the class
     * @throws ObjenesisException is something goes wrong
     */
    private static byte[] writeExtendingClass(Class<?> type, String suffix) {
        String parentClazz = ClassDefinitionUtils.classNameToInternalClassName(type.getName());
        String clazz = parentClazz + suffix;

        DataOutputStream in = null;
        ByteArrayOutputStream bIn = new ByteArrayOutputStream(1000); // 1000 should be large enough to fit the entire class
        try {
            in = new DataOutputStream(bIn);

            in.write(ClassDefinitionUtils.MAGIC);
            in.write(ClassDefinitionUtils.VERSION);
            in.writeShort(CONSTANT_POOL_COUNT);

            // set all the constant pool here

            // 1. class
            in.writeByte(ClassDefinitionUtils.CONSTANT_Class);
            in.writeShort(INDEX_UTF8_CLASS);

            // 2. super class
            in.writeByte(ClassDefinitionUtils.CONSTANT_Class);
            in.writeShort(INDEX_UTF8_SUPERCLASS);

            // 3. default constructor name
            in.writeByte(ClassDefinitionUtils.CONSTANT_Utf8);
            in.writeUTF(CONSTRUCTOR_NAME);

            // 4. default constructor description
            in.writeByte(ClassDefinitionUtils.CONSTANT_Utf8);
            in.writeUTF(CONSTRUCTOR_DESC);

            // 5. Code
            in.writeByte(ClassDefinitionUtils.CONSTANT_Utf8);
            in.writeUTF("Code");

            // 6. Class name
            in.writeByte(ClassDefinitionUtils.CONSTANT_Utf8);
            in.writeUTF("L" + clazz + ";");

            // 7. Class name (again)
            in.writeByte(ClassDefinitionUtils.CONSTANT_Utf8);
            in.writeUTF(clazz);

            // 8. Superclass name
            in.writeByte(ClassDefinitionUtils.CONSTANT_Utf8);
            in.writeUTF(parentClazz);

            // end of constant pool

            // access flags: We want public, ACC_SUPER is always there
            in.writeShort(ClassDefinitionUtils.ACC_PUBLIC | ClassDefinitionUtils.ACC_SUPER);

            // this class index in the constant pool
            in.writeShort(INDEX_CLASS_THIS);

            // super class index in the constant pool
            in.writeShort(INDEX_CLASS_SUPERCLASS);

            // interfaces implemented count (we have none)
            in.writeShort(0);

            // fields count (we have none)
            in.writeShort(0);

            // methods count (we have one: the default constructor)
            in.writeShort(1);

            // default constructor method_info
            in.writeShort(ClassDefinitionUtils.ACC_PUBLIC);
            in.writeShort(INDEX_UTF8_CONSTRUCTOR_NAME); // index of the method name (<init>)
            in.writeShort(INDEX_UTF8_CONSTRUCTOR_DESC); // index of the description
            in.writeShort(1); // number of attributes: only one, the code

            // code attribute of the default constructor
            in.writeShort(INDEX_UTF8_CODE_ATTRIBUTE);
            in.writeInt(CODE_ATTRIBUTE_LENGTH); // attribute length
            in.writeShort(1); // max_stack
            in.writeShort(1); // max_locals
            in.writeInt(CODE.length); // code length
            in.write(CODE);
            in.writeShort(0); // exception_table_length = 0
            in.writeShort(0); // attributes count = 0, no need to have LineNumberTable and LocalVariableTable

            // class attributes
            in.writeShort(0); // none. No need to have a source file attribute

        } catch (IOException e) {
            throw new ObjenesisException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new ObjenesisException(e);
                }
            }
        }

        return bIn.toByteArray();
    }
}
