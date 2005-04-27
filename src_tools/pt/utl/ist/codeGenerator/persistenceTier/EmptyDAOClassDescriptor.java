package pt.utl.ist.codeGenerator.persistenceTier;

import java.io.IOException;
import java.lang.reflect.Method;

import pt.utl.ist.codeGenerator.ClassDescriptor;
import pt.utl.ist.codeGenerator.MethodBodyClosure;
import pt.utl.ist.util.CollectionConstructors;

public class EmptyDAOClassDescriptor extends DAOClassDescriptor {

    protected static final MethodBodyClosure DAO_DELEGATOR_METHOD_CLOSURE = new MethodBodyClosure() {

        public String generateBody(Method method) {
            final StringBuilder stringBuilder = new StringBuilder();

            if (!method.getReturnType().getName().equals("void")) {
                stringBuilder.append("\t\treturn ");
                stringBuilder.append(getDefaultValue(method.getReturnType()));
                stringBuilder.append(";\n");
            }

            return stringBuilder.toString();
        }
    };

    public EmptyDAOClassDescriptor(final String absoluteClazzName, final Class daoInterface) {
        super(absoluteClazzName, daoInterface, DAO_DELEGATOR_METHOD_CLOSURE);
    }

    public String customCode() {
        return null;
    }

}
