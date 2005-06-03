package pt.utl.ist.codeGenerator.persistenceTier;

import java.io.IOException;
import java.lang.reflect.Method;

import net.sourceforge.fenixedu.persistenceTier.delegatedObjects.DAOResultComparator;
import net.sourceforge.fenixedu.persistenceTier.delegatedObjects.DAOResultLogger;
import pt.utl.ist.codeGenerator.ClassDescriptor;
import pt.utl.ist.codeGenerator.MethodBodyClosure;
import pt.utl.ist.util.CollectionConstructors;

public class DAOClassDescriptor extends ClassDescriptor {

    protected static final MethodBodyClosure DAO_DELEGATOR_METHOD_CLOSURE = new MethodBodyClosure() {

        final String MAIN_DAO = "mainDAO";

        final String SECONDARY_DAO = "secondaryDAO";

        final String MAIN_DAO_RESULT = "mainDAOResult";

        final String SECONDARY_DAO_RESULT = "secondaryDAOResult";

        final String COMPARISON_RESULT = "sameResult";

        public String generateBody(Method method) {
            final StringBuilder stringBuilder = new StringBuilder();

            if (!method.getReturnType().getName().equals("void")) {
                generateMethodCall(stringBuilder, MAIN_DAO_RESULT, MAIN_DAO, method);
                generateMethodCall(stringBuilder, SECONDARY_DAO_RESULT, SECONDARY_DAO, method);
            } else {
                generateMethodCall(stringBuilder, MAIN_DAO_RESULT, MAIN_DAO, method);
            }

            if (!method.getReturnType().getName().equals("void")) {
                compareDAOResults(stringBuilder);
                logResultComparison(stringBuilder, method);
                logResultComparisonOK(stringBuilder, method);
                stringBuilder.append("\n\t\treturn mainDAOResult;\n");
            }

            return stringBuilder.toString();
        }

        private void compareDAOResults(final StringBuilder stringBuilder) {
            stringBuilder.append("\n\t\tfinal boolean ");
            stringBuilder.append(COMPARISON_RESULT);
            stringBuilder.append(" = ");

            stringBuilder.append(getSimpleClassName(DAOResultComparator.class.getName()));
            stringBuilder.append(".compare(");
            stringBuilder.append(MAIN_DAO_RESULT);
            stringBuilder.append(",");
            stringBuilder.append(SECONDARY_DAO_RESULT);
            stringBuilder.append(");\n");
        }

        private void logResultComparison(final StringBuilder stringBuilder, Method method) {
            stringBuilder.append("\t\tif (!");
            stringBuilder.append(COMPARISON_RESULT);
            stringBuilder.append(") {\n");
            stringBuilder.append("\t\t\tfinal Object[] parameters = new Object[] {\n");

            int i = 0;
            for (final Class parameterType : method.getParameterTypes()) {
                if (i++ > 0) {
                    stringBuilder.append(",\n ");
                }
                stringBuilder.append("\t\t\t\targ");
                stringBuilder.append(i);
            }
            stringBuilder.append("\n");

            stringBuilder.append("\t\t\t};\n");
            stringBuilder.append("\t\t\t");
            stringBuilder.append(getSimpleClassName(DAOResultLogger.class.getName()));
            stringBuilder.append(".log(");
            stringBuilder.append(MAIN_DAO);
            stringBuilder.append(", ");
            stringBuilder.append(SECONDARY_DAO);
            stringBuilder.append(", \"");
            stringBuilder.append(method.getName());            
            stringBuilder.append("\", parameters);\n\t\t}\n");
        }

        private void logResultComparisonOK(final StringBuilder stringBuilder, Method method) {
            stringBuilder.append("\t\tif (");
            stringBuilder.append(COMPARISON_RESULT);
            stringBuilder.append(") {\n");
            stringBuilder.append("\t\t\tfinal Object[] parameters = new Object[] {\n");

            int i = 0;
            for (final Class parameterType : method.getParameterTypes()) {
                if (i++ > 0) {
                    stringBuilder.append(",\n ");
                }
                stringBuilder.append("\t\t\t\targ");
                stringBuilder.append(i);
            }
            stringBuilder.append("\n");

            stringBuilder.append("\t\t\t};\n");
            stringBuilder.append("\t\t\t");
            stringBuilder.append(getSimpleClassName(DAOResultLogger.class.getName()));
            stringBuilder.append(".logOK(");
            stringBuilder.append(MAIN_DAO);
            stringBuilder.append(", ");
            stringBuilder.append(SECONDARY_DAO);
            stringBuilder.append(", \"");
            stringBuilder.append(method.getName());            
            stringBuilder.append("\", parameters);\n\t\t}\n");
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
//            if (method.getReturnType().getName().equals("void")) {
//                stringBuilder.append(" else if (");
//                stringBuilder.append(MAIN_DAO);
//                stringBuilder.append(" != null) {\n");
//                stringBuilder.append("\t\t\t");
//
//                stringBuilder.append(MAIN_DAO);
//                stringBuilder.append(".");
//                stringBuilder.append(getSimpleClassName(method.getName()));
//                stringBuilder.append("(");
//                int j = 0;
//                for (final Class parameterType : method.getParameterTypes()) {
//                    if (j++ > 0) {
//                        stringBuilder.append(", ");
//                    }
//                    stringBuilder.append("arg");
//                    stringBuilder.append(j);
//                }
//                stringBuilder.append(");\n\t\t}");
//            }
            stringBuilder.append("\n");
        }

    };

    private final String absoluteClazzName;

    private final Class daoInterface;

    private final MethodBodyClosure methodBodyClosure;

    public DAOClassDescriptor(final String absoluteClazzName, final Class daoInterface,
            final MethodBodyClosure methodBodyClosure) {
        super(absoluteClazzName, null, CollectionConstructors.newHashSet(daoInterface));
        this.absoluteClazzName = absoluteClazzName;
        this.daoInterface = daoInterface;
        this.methodBodyClosure = methodBodyClosure;
        addImport(DAOResultComparator.class);
        addImport(DAOResultLogger.class);
    }

    public DAOClassDescriptor(final String absoluteClazzName, final Class daoInterface) {
        this(absoluteClazzName, daoInterface, DAO_DELEGATOR_METHOD_CLOSURE);
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
        addMethod(method, methodBodyClosure);
    }

    public static String getDefaultValue(Class clazz) {
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

}
