package pt.utl.ist.codeGenerator.persistenceTier;

import java.io.IOException;
import java.lang.reflect.Method;

import pt.utl.ist.codeGenerator.ClassDescriptor;
import pt.utl.ist.codeGenerator.MethodBodyClosure;

public class DAOClassDescriptor extends ClassDescriptor {

    private static final MethodBodyClosure DAO_DELEGATOR_METHOD_CLOSURE = new MethodBodyClosure() {

        public String generateBody(Method method) {
            final StringBuilder stringBuilder = new StringBuilder();

            generateMethodCall(stringBuilder, "mainDAOResult", "mainDAO", method);
            generateMethodCall(stringBuilder, "secondaryDAOResult", "secondaryDAO", method);

            if (!method.getReturnType().getName().equals("void")) {
                stringBuilder.append("\n\t\treturn mainDAOResult;\n");
            }

            return stringBuilder.toString();
        }

        private void generateMethodCall(final StringBuilder stringBuilder, final String resultVariable,
                final String dao, final Method method) {
            if (!method.getReturnType().getName().equals("void")) {
                stringBuilder.append("\t\tfinal ");
                stringBuilder.append(getSimpleClassName(method.getReturnType().getName()));
                stringBuilder.append(" ");
                stringBuilder.append(resultVariable);
                stringBuilder.append(";\n");
            }
            stringBuilder.append("\t\tif (");
            stringBuilder.append(dao);
            stringBuilder.append(" != null) {\n");
            stringBuilder.append("\t\t\t");

            if (!method.getReturnType().getName().equals("void")) {
                stringBuilder.append(resultVariable);
                stringBuilder.append(" = ");
            }

            stringBuilder.append(dao);
            stringBuilder.append(".");
            stringBuilder.append(getSimpleClassName(method.getName()));
            stringBuilder.append("(");
            int i = 0;
            for (final Class parameterType : method.getParameterTypes()) {
                if (i++ > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append("arg");
                stringBuilder.append(i);
            }
            stringBuilder.append(");\n");

            stringBuilder.append("\t\t}");
            if (!method.getReturnType().getName().equals("void")) {
                stringBuilder.append(" else {\n");
                stringBuilder.append("\t\t\t");
                stringBuilder.append(resultVariable);
                stringBuilder.append(" = ");

                stringBuilder.append(getDefaultValue(method.getReturnType()));

                stringBuilder.append(";\n\t\t}");
            }
            stringBuilder.append("\n");
        }

        private String getDefaultValue(Class clazz) {
            if (!clazz.isPrimitive()) {
                return "null";
            }
            if (clazz.getName().equals("int")) {
                return "-1";
            }
            if (clazz.getName().equals("boolean")) {
                return "false";
            }
            return "null";
        }

    };

    private final String absoluteClazzName;

    private final Class daoInterface;

    public DAOClassDescriptor(final String absoluteClazzName, final Class daoInterface) {
        super(absoluteClazzName, null, implementsClazzes(daoInterface));
        this.absoluteClazzName = absoluteClazzName;
        this.daoInterface = daoInterface;
    }

    public String customCode() {
        final StringBuilder stringBuilder = new StringBuilder();

        final String simpleDAOInterfaceName = getSimpleClassName(daoInterface.getName());

        stringBuilder.append("\tprivate final ");
        stringBuilder.append(simpleDAOInterfaceName);
        stringBuilder.append(" mainDAO;\n");
        stringBuilder.append("\tprivate final ");
        stringBuilder.append(simpleDAOInterfaceName);
        stringBuilder.append(" secondaryDAO;\n\n");

        stringBuilder.append("\tpublic ");
        stringBuilder.append(getSimpleClassName(absoluteClazzName));
        stringBuilder.append("(");
        stringBuilder.append(simpleDAOInterfaceName);
        stringBuilder.append(" mainDAO, ");
        stringBuilder.append(simpleDAOInterfaceName);
        stringBuilder.append(" secondaryDAO) {\n");
        stringBuilder.append("\t\tthis.mainDAO = mainDAO;\n");
        stringBuilder.append("\t\tthis.secondaryDAO = secondaryDAO;\n");
        stringBuilder.append("\t}\n\n");

        return stringBuilder.toString();
    }

    public void addMethod(final Method method) throws IOException {
        addMethod(method, DAO_DELEGATOR_METHOD_CLOSURE);
    }

}
