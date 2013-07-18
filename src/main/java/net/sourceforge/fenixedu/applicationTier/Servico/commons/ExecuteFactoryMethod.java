package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.services.Service;

public class ExecuteFactoryMethod {

    @Service
    public static Object run(FactoryExecutor factoryExecutor) {
        if (factoryExecutor != null) {
            return factoryExecutor.execute();
        } else {
            return null;
        }
    }

}