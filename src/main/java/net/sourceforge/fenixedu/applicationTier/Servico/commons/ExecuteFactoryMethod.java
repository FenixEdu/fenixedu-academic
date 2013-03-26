package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.services.Service;

public class ExecuteFactoryMethod extends FenixService {

    @Service
    public static Object run(FactoryExecutor factoryExecutor) {
        if (factoryExecutor != null) {
            return factoryExecutor.execute();
        } else {
            return null;
        }
    }

}