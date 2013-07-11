package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixframework.Atomic;

public class ExecuteFactoryMethod {

    @Atomic
    public static Object run(FactoryExecutor factoryExecutor) {
        if (factoryExecutor != null) {
            return factoryExecutor.execute();
        } else {
            return null;
        }
    }

}