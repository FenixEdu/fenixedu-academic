package pt.utl.ist.codeGenerator.persistenceTier;

import java.io.IOException;
import java.lang.reflect.Method;

import pt.utl.ist.codeGenerator.ClassDescriptor;
import pt.utl.ist.codeGenerator.MethodBodyClosure;
import pt.utl.ist.util.CollectionConstructors;

public class PersistenceSupportClassDescriptor extends ClassDescriptor {

    private final String absoluteClazzName;

    protected final MethodBodyClosure methodBodyClosure;

    final Class mainPersistenceSupportClazz;

    final Class secondaryPersistenceSupportClazz;

    public PersistenceSupportClassDescriptor(final String absoluteClazzName, final Class extendsClazz,
            final Class implementsClazz) {
        super(absoluteClazzName, extendsClazz, CollectionConstructors.newHashSet(implementsClazz));
        this.absoluteClazzName = absoluteClazzName;
        methodBodyClosure = new NullMethodBodyClosure();
        this.mainPersistenceSupportClazz = null;
        this.secondaryPersistenceSupportClazz = null;
    }

    public PersistenceSupportClassDescriptor(final String absoluteClazzName, final Class extendsClazz,
            final Class implementsClazz, final Class mainPersistenceSupportClazz,
            final Class secondaryPersistenceSupportClazz) {
        super(absoluteClazzName, extendsClazz, CollectionConstructors.newHashSet(implementsClazz));
        this.absoluteClazzName = absoluteClazzName;
        methodBodyClosure = new WrapperMethodBodyClosure(mainPersistenceSupportClazz,
                secondaryPersistenceSupportClazz);
        this.mainPersistenceSupportClazz = mainPersistenceSupportClazz;
        this.secondaryPersistenceSupportClazz = secondaryPersistenceSupportClazz;
        if (mainPersistenceSupportClazz != null) {
            addImport(mainPersistenceSupportClazz);
        }
        if (secondaryPersistenceSupportClazz != null) {
            addImport(secondaryPersistenceSupportClazz);
        }
    }

    public String customCode() {
        final StringBuilder stringBuilder = new StringBuilder();

        final String simpleClazzName = getSimpleClassName(absoluteClazzName);

        stringBuilder.append("\tprivate static final ");
        stringBuilder.append(simpleClazzName);
        stringBuilder.append(" instance = new ");
        stringBuilder.append(simpleClazzName);
        stringBuilder.append("();\n\n");

        if (mainPersistenceSupportClazz != null) {
            stringBuilder.append("\tprivate static final ");
            stringBuilder.append(getSimpleClassName(mainPersistenceSupportClazz.getName()));
            stringBuilder.append(" mainPersistenceSupport;\n");
        }
        if (secondaryPersistenceSupportClazz != null) {
            stringBuilder.append("\tprivate static final ");
            stringBuilder.append(getSimpleClassName(secondaryPersistenceSupportClazz.getName()));
            stringBuilder.append(" secondaryPersistenceSupport;\n");
        }

        stringBuilder.append("\n\tstatic {\n");
        if (mainPersistenceSupportClazz != null || secondaryPersistenceSupportClazz != null) {
            stringBuilder.append("\t\ttry {\n");
            if (mainPersistenceSupportClazz != null) {
                stringBuilder.append("\t\t\tmainPersistenceSupport = ");
                stringBuilder.append(getSimpleClassName(mainPersistenceSupportClazz.getName()));
                stringBuilder.append(".getInstance();\n");
            }
            if (secondaryPersistenceSupportClazz != null) {
                stringBuilder.append("\t\t\tsecondaryPersistenceSupport = ");
                stringBuilder.append(getSimpleClassName(secondaryPersistenceSupportClazz.getName()));
                stringBuilder.append(".getInstance();\n");
            }
            stringBuilder.append("\t\t} catch(Exception ex) {\n");
            stringBuilder.append("\t\t\tthrow new RuntimeException(ex);\n");
            stringBuilder.append("\t\t}\n");
        }
        stringBuilder.append("\t}\n\n");

        stringBuilder.append("\tprivate ");
        stringBuilder.append(simpleClazzName);
        stringBuilder.append("() {}\n\n");

        stringBuilder.append("\tpublic static ");
        stringBuilder.append(simpleClazzName);
        stringBuilder.append(" getInstance() {\n");
        stringBuilder.append("\t\treturn instance;\n");
        stringBuilder.append("\t}\n\n");

        return stringBuilder.toString();
    }

    public void addMethod(final Method method) throws IOException {
        addMethod(method, methodBodyClosure);

        if (!method.getReturnType().isPrimitive() && !method.getReturnType().getName().startsWith("java.lang.")) {
            createDAO(method.getReturnType());
        }
    }

    private class NullMethodBodyClosure implements MethodBodyClosure {
        public String generateBody(final Method method) {
            final StringBuilder stringBuilder = new StringBuilder();
            if (!method.getReturnType().isPrimitive()) {
                stringBuilder.append("\t\treturn null;\n");
            } else {
                stringBuilder.append("\t\treturn ");
                stringBuilder.append(returnValue(method.getReturnType()));
                stringBuilder.append(";\n");
            }
            return stringBuilder.toString();
        }

        private String returnValue(Class returnType) {
            if (returnType.getName().equals("void")) {
                return "";
            }
            if (returnType.getName().equals("int")) {
                return "0";
            }
            return null;
        }
    }

    private class WrapperMethodBodyClosure implements MethodBodyClosure {

        final Class mainPersistenceSupportClazz;

        final Class secondaryPersistenceSupportClazz;

        public WrapperMethodBodyClosure(final Class mainPersistenceSupportClazz,
                final Class secondaryPersistenceSupportClazz) {
            this.mainPersistenceSupportClazz = mainPersistenceSupportClazz;
            this.secondaryPersistenceSupportClazz = secondaryPersistenceSupportClazz;
        }

        public String generateBody(final Method method) {
            final StringBuilder stringBuilder = new StringBuilder();

            final String returnType = returnType(method.getReturnType());

            if (mainPersistenceSupportClazz != null) {
                generateMethodCall("mainDAO", "mainPersistenceSupport", method, stringBuilder,
                        returnType, mainPersistenceSupportClazz);
            }
            if (secondaryPersistenceSupportClazz != null) {
                generateMethodCall("secondaryDAO", "secondaryPersistenceSupport", method, stringBuilder,
                        returnType, secondaryPersistenceSupportClazz);
            }

            if (returnType != null) {
                if (!method.getReturnType().isPrimitive() && !method.getReturnType().getName().startsWith("java.lang.")) {
                    stringBuilder.append("\t\treturn new ");
                    stringBuilder.append(getDelegateDAOFromReturnType(method.getReturnType()));
                    stringBuilder.append("(");
                    if (mainPersistenceSupportClazz != null) {
                        stringBuilder.append("mainDAO");
                    } else {
                        stringBuilder.append("null");
                    }
                    stringBuilder.append(", ");
                    if (secondaryPersistenceSupportClazz != null) {
                        stringBuilder.append("secondaryDAO");
                    } else {
                        stringBuilder.append("null");
                    }
                    stringBuilder.append(");\n");
                } else {
                    stringBuilder.append("\t\treturn mainDAO; // not realy a DAO\n");
                }
            }

            return stringBuilder.toString();
        }

        private void generateMethodCall(final String variableName,
                final String persistenceSupportVariableName, final Method method,
                final StringBuilder stringBuilder, final String returnType,
                final Class persistenceSupportClazz) {
            if (returnType != null) {
                stringBuilder.append("\t\tfinal ");
                stringBuilder.append(returnType);
                stringBuilder.append(" ");
                stringBuilder.append(variableName);
                stringBuilder.append(" = ");
            } else {
                stringBuilder.append("\t\t");
            }
            stringBuilder.append(persistenceSupportVariableName);
            stringBuilder.append(".");
            stringBuilder.append(method.getName());
            stringBuilder.append("(");
            int i = 0;
            for (final Class parameterType : method.getParameterTypes()) {
                if (i++ > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(getSimpleClassName(parameterType.getName()));
                stringBuilder.append(" arg");
                stringBuilder.append(i);
            }
            stringBuilder.append(");\n");
        }

        private String returnType(final Class returnType) {
            if (returnType.getName().equals("void")) {
                return null;
            }
            if (returnType.isPrimitive()) {
                return returnType.getName();
            }
            return getSimpleClassName(returnType.getName());
        }

    }

    private void createDAO(final Class returnType) throws IOException {
        final String src_gen = "src_gen";
        final String packageName = getPackageName(absoluteClazzName);
        final String absoluteDAOClazzName = packageName + "." + getDelegateDAOFromReturnType(returnType);

        final DAOClassDescriptor classDescriptor;
        if (mainPersistenceSupportClazz != null) {
            classDescriptor = new DAOClassDescriptor(absoluteDAOClazzName, returnType);
        } else {
            classDescriptor = new EmptyDAOClassDescriptor(absoluteDAOClazzName, returnType);
        }
        classDescriptor.addUnimplementedMethods();
        classDescriptor.writeToFile(src_gen);
    }

    public String getDelegateDAOFromReturnType(final Class returnType) {
        return getSimpleClassName(returnType.getName()).substring(1) + "Delegate";
    }

}
