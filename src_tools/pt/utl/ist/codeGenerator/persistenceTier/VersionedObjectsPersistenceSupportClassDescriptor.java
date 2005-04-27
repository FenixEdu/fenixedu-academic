package pt.utl.ist.codeGenerator.persistenceTier;

import java.lang.reflect.Method;

import pt.utl.ist.codeGenerator.MethodBodyClosure;

public class VersionedObjectsPersistenceSupportClassDescriptor extends PersistenceSupportClassDescriptor {

    protected final MethodBodyClosure methodBodyClosure = new MethodBodyClosure() {
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
    };

    public VersionedObjectsPersistenceSupportClassDescriptor(final String absoluteClazzName, final Class extendsClazz,
            final Class implementsClazz) {
        super(absoluteClazzName, extendsClazz, implementsClazz);
    }

}
