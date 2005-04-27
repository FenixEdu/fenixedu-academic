package pt.utl.ist.codeGenerator;

import java.lang.reflect.Method;

public interface MethodBodyClosure {

    public String generateBody(final Method method);

}
