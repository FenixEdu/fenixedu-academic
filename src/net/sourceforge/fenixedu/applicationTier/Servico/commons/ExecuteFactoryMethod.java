package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ExecuteFactoryMethod extends FenixService {

    public Object run(FactoryExecutor factoryExecutor) {
	if (factoryExecutor != null) {
	    return factoryExecutor.execute();
	} else {
	    return null;
	}
    }

}
