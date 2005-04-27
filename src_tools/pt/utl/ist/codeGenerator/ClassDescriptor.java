package pt.utl.ist.codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import pt.utl.ist.util.StringAppender;

public class ClassDescriptor {

    private class MethodDescriptor {
        final Method method;

        final MethodBodyClosure methodBodyClosure;

        private MethodDescriptor(final Method method, final MethodBodyClosure methodBodyClosure) {
            this.method = method;
            this.methodBodyClosure = methodBodyClosure;

            addImport(method.getReturnType());
            addImports(method.getParameterTypes());
            addImports(method.getExceptionTypes());
        }

        public String toString() {
            final StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("\t");
            stringBuilder.append(modifier(method.getModifiers()));
            stringBuilder.append(" ");
            stringBuilder.append(returnClassAsString(method.getReturnType()));
            stringBuilder.append(" ");
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
            stringBuilder.append(") ");

            if (method.getExceptionTypes().length > 0) {
                stringBuilder.append("throws ");
                int j = 0;
                for (final Class exceptionClass : method.getExceptionTypes()) {
                    if (j++ > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(getSimpleClassName(exceptionClass.getName()));
                }
            }

            stringBuilder.append(" {\n");

            if (methodBodyClosure != null) {
                stringBuilder.append(methodBodyClosure.generateBody(method));
            }

            stringBuilder.append("\t}");

            return stringBuilder.toString();
        }

        private String returnClassAsString(Class returnType) {
            if (returnType != null) {
                return getSimpleClassName(returnType.getName());
            }
            return "void";
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
            throw new RuntimeException("Unsupported method modifier: " + modifiers);
        }
    }

    final String packageName;

    final Set<String> imports = new HashSet<String>();

    final String clazzName;

    final String extendsClazzName;

    final Set<String> implementsClazzNames = new HashSet<String>();

    final Set<MethodDescriptor> methodDescriptors = new HashSet<MethodDescriptor>();

    final Set<Class> implementsClazzes;

    public ClassDescriptor(final String absoluteClazzName, final Class extendsClazz,
            final Set<Class> implementsClazzes) {
        packageName = getPackageName(absoluteClazzName);
        clazzName = getSimpleClassName(absoluteClazzName);
        this.implementsClazzes = implementsClazzes;

        if (extendsClazz != null) {
            addImport(extendsClazz);
            extendsClazzName = getSimpleClassName(extendsClazz.getName());
        } else {
            extendsClazzName = null;
        }

        if (implementsClazzes != null) {
            for (final Class implementsClaszz : implementsClazzes) {
                addImport(implementsClaszz);
                implementsClazzNames.add(getSimpleClassName(implementsClaszz.getName()));
            }
        }
    }

    public void addUnimplementedMethods() throws IOException {
        for (final Class implementsClazz : implementsClazzes) {
            final Method[] methods = implementsClazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                final Method method = methods[i];
                addMethod(method);
            }
        }
    }

    public void addMethod(final Method method) throws IOException {
        addMethod(method, null);
    }

    protected void addMethod(final Method method, final MethodBodyClosure methodBodyClosure) {
        methodDescriptors.add(new MethodDescriptor(method, methodBodyClosure));
    }

    public void addImport(final Class clazz) {
        if (clazz != null && !clazz.isPrimitive() && !clazz.getPackage().getName().equals("java.lang")) {
            imports.add(clazz.getName());
        }
    }

    public void addImports(final Class[] clazzes) {
        for (int i = 0; i < clazzes.length; i++) {
            addImport(clazzes[i]);
        }
    }

    public static String getSimpleClassName(final String completeClassName) {
        final int lastDotIndex = completeClassName.lastIndexOf('.');
        return completeClassName.substring(lastDotIndex + 1);
    }

    public String getPackageName(final String completeClassName) {
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

        String customCode = customCode();
        if (customCode != null) {
            stringBuilder.append(customCode);
        }

        for (final MethodDescriptor methodDescriptor : methodDescriptors) {
            stringBuilder.append(methodDescriptor.toString());
            stringBuilder.append("\n\n");
        }

        stringBuilder.append("}\n");

        return stringBuilder.toString();
    }

    public String customCode() {
        return null;
    }

    private String absoluteClassName() {
        return StringAppender.append(packageName, ".", clazzName);
    }

    public void writeToFile(final String dir) throws IOException {
        final String absoluteFileName = StringAppender.append(dir, "/", absoluteClassName().replaceAll("\\.", "/"), ".java");
        final File file = new File(absoluteFileName);
        file.getParentFile().mkdirs();
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(toString());
        fileWriter.close();
    }

}
