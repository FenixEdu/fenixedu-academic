package pt.utl.ist.codeGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DAOGenerator {

    private static final Logger logger = Logger.getLogger(DAOGenerator.class.getName());

    private class ClassDescriptor {

        private class MethodDescriptor {
            final String modifier;
            final String returnClazzName;
            final String methodName;
            final List<String> parameterClazzNames = new ArrayList<String>();
            final List<String> thrownExceptions = new ArrayList<String>();

            private MethodDescriptor(final Method method) {
                this.modifier = modifier(method.getModifiers());

                if (method.getReturnType() != null) {
                    if (!method.getReturnType().isPrimitive()) {
                        imports.add(method.getReturnType().getName());
                    }
                    this.returnClazzName = getSimpleClassName(method.getReturnType().getName());
                } else {
                    this.returnClazzName = "void";
                }

                this.methodName = method.getName();

                final Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null) {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!parameterTypes[i].isPrimitive()) {
                            imports.add(parameterTypes[i].getName());
                        }
                        this.parameterClazzNames.add(getSimpleClassName(parameterTypes[i].getName()));
                    }
                }

                final Class[] excpetionTypes = method.getExceptionTypes();
                if (excpetionTypes != null) {
                    for (int i = 0; i < excpetionTypes.length; i++) {
                        imports.add(excpetionTypes[i].getName());
                        this.thrownExceptions.add(getSimpleClassName(excpetionTypes[i].getName()));
                    }
                }
            }

            public String toString() {
                final StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("\t");
                stringBuilder.append(modifier);
                stringBuilder.append(" ");
                stringBuilder.append(returnClazzName);
                stringBuilder.append(" ");
                stringBuilder.append(methodName);
                stringBuilder.append("(");
                int i = 0;
                for (final String parameterClazzName : parameterClazzNames) {
                    if (i++ > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(parameterClazzName);
                    stringBuilder.append(" arg");
                    stringBuilder.append(i);
                }
                stringBuilder.append(") ");

                if (thrownExceptions.size() > 0) {
                    stringBuilder.append("throws ");
                    int j = 0;
                    for (final String thrownException : thrownExceptions) {
                        if (j++ > 0) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(thrownException);
                    }
                }

                stringBuilder.append(" {\n");
                if (clazzToWrapName != null) {
                    stringBuilder.append("\t\t");
                    if (!returnClazzName.equals("void")) {
                        stringBuilder.append("return ");
                    }
                    stringBuilder.append("wrappedInstance.");

                    stringBuilder.append(methodName);
                    stringBuilder.append("(");
                    int k = 0;
                    for (final String parameterClazzName : parameterClazzNames) {
                        if (k++ > 0) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append("arg");
                        stringBuilder.append(k);
                    }

                    stringBuilder.append(");\n");
                }

                stringBuilder.append("\t}");

                return stringBuilder.toString();
            }

            private String modifier(int modifiers) {
                if (Modifier.isPublic(modifiers)) {
                    return "public";
                }
                if (Modifier.isProtected(modifiers)) {
                    return "protected";
                }
                if (Modifier.isPrivate(modifiers)) {
                    return "private";
                }
                throw new RuntimeException("Unsupported method modifier: " + modifier);
            }
        }

        final String packageName;

        final Set<String> imports = new HashSet<String>();

        final String clazzName;

        final String extendsClazzName;

        final Set<String> implementsClazzNames = new HashSet<String>();;

        final Set<MethodDescriptor> methodDescriptors = new HashSet<MethodDescriptor>();

        final String clazzToWrapName;

        private ClassDescriptor(final String absoluteClazzName, final String absoluteExtendsClassName,
                final String absoluteImplementsClazzNames, final String nameClaszzToWrap) {
            packageName = getPackageName(absoluteClazzName);
            clazzName = getSimpleClassName(absoluteClazzName);

            if (absoluteExtendsClassName != null && absoluteExtendsClassName.length() > 0) {
                if (absoluteExtendsClassName.contains(".")) {
                    imports.add(absoluteExtendsClassName);
                }
                extendsClazzName = getSimpleClassName(absoluteExtendsClassName);
            } else {
                extendsClazzName = null;
            }

            final String[] absoluteImplementsClazzNamesArray = absoluteImplementsClazzNames.split(",");
            for (int i = 0; i < absoluteImplementsClazzNamesArray.length; i++) {
                final String absoluteImplementsClazzName = absoluteImplementsClazzNamesArray[i];
                imports.add(absoluteImplementsClazzName);
                implementsClazzNames.add(getSimpleClassName(absoluteImplementsClazzName));
            }

            if (nameClaszzToWrap != null && nameClaszzToWrap.length() > 0) {
                if (nameClaszzToWrap.contains(".")) {
                    imports.add(nameClaszzToWrap);
                }
                clazzToWrapName = getSimpleClassName(nameClaszzToWrap);
            } else {
                clazzToWrapName = null;
            }
        }

        private void addMethod(final Method method) {
            methodDescriptors.add(new MethodDescriptor(method));
        }

        private String getSimpleClassName(final String completeClassName) {
            final int lastDotIndex = completeClassName.lastIndexOf('.');
            return completeClassName.substring(lastDotIndex + 1);
        }

        private String getPackageName(final String completeClassName) {
            final int lastDotIndex = completeClassName.lastIndexOf('.');
            return completeClassName.substring(0, lastDotIndex);
        }

        public String toString() {
            final StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("package ");
            stringBuilder.append(packageName);
            stringBuilder.append(";\n\n");

            for (final String someImport : imports) {
                stringBuilder.append("import ");
                stringBuilder.append(someImport);
                stringBuilder.append(";\n");
            }

            stringBuilder.append("\npublic class ");
            stringBuilder.append(clazzName);

            if (extendsClazzName != null) {
                stringBuilder.append(" extends ");
                stringBuilder.append(extendsClazzName);
            }

            if (implementsClazzNames.size() > 0) {
                stringBuilder.append(" implements");
                int i = 0;
                for (final String implementsClaszzName : implementsClazzNames) {
                    if (i++ > 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(" ");
                    stringBuilder.append(implementsClaszzName);
                }
            }

            stringBuilder.append(" {\n\n");

            stringBuilder.append("\tprivate static final ");
            stringBuilder.append(clazzToWrapName);
            stringBuilder.append(" wrappedInstance;\n\n");
            stringBuilder.append("\tstatic{\n");
            stringBuilder.append("\t\ttry {");
            stringBuilder.append("\n\t\t\twrappedInstance = ");
            stringBuilder.append(clazzToWrapName);
            stringBuilder.append(".getInstance();\n");
            stringBuilder.append("\t\t} catch(Exception ex) {\n");
            stringBuilder.append("\t\t\tthrow new RuntimeException(ex);\n");
            stringBuilder.append("\t\t}\n");
            stringBuilder.append("\t}\n\n");

            for (final MethodDescriptor methodDescriptor : methodDescriptors) {
                stringBuilder.append(methodDescriptor.toString());
                stringBuilder.append("\n\n");
            }

            stringBuilder.append("}\n");
            

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        final String src_gen = "src_gen";
        final String namePersistenceClaszzToCreate = "net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport";
        final String nameClazzToImplement = "net.sourceforge.fenixedu.persistenceTier.ISuportePersistente";

        final String namePersistenceClaszzToReplicate = "net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB";

        try {
            final DAOGenerator generator = new DAOGenerator();
            final String persistenceSupportClazzContents = generator.generatePersistenceSupportClazz(namePersistenceClaszzToCreate,
                    nameClazzToImplement, namePersistenceClaszzToReplicate);
            final String persistenceSupportClazzFilename = src_gen + "/" + namePersistenceClaszzToCreate.replaceAll("\\.", "/") + ".java";
            write(persistenceSupportClazzFilename, persistenceSupportClazzContents);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Generation Complete.");
    }

    private String generatePersistenceSupportClazz(final String namePersistenceClaszzToCreate,
            final String nameClazzToImplement, final String namePersistenceClaszzToReplicate)
            throws ClassNotFoundException {

        final ClassDescriptor classDescriptor = new ClassDescriptor(namePersistenceClaszzToCreate, "java.lang.Object", nameClazzToImplement, namePersistenceClaszzToReplicate);

        final Class persistenceClaszzToReplicate = Class.forName(nameClazzToImplement);
        final Method[] methods = persistenceClaszzToReplicate.getMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            classDescriptor.addMethod(method);
        }

        return classDescriptor.toString();
    }

    private static void write(final String filename, final String fileContents) throws IOException {
        System.out.println("filename = " + filename);
        final File file = new File(filename);
        file.getParentFile().mkdirs();
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContents);
        fileWriter.close();
    }

}
