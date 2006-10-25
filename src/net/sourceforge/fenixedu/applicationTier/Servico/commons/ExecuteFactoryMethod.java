package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ExecuteFactoryMethod extends Service {

    public Object run(FactoryExecutor factoryExecutor) throws ExcepcaoPersistencia {
	if (factoryExecutor != null) {
	    return factoryExecutor.execute();
	} else {
	    return null;
	}
    }

}
