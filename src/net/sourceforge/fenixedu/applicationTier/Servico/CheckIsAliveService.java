package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CheckIsAliveService implements IService {

	public Boolean run() throws ExcepcaoPersistencia {
		checkFenixDatabaseOps();
		return new Boolean(true);
	}

	private void checkFenixDatabaseOps() throws ExcepcaoPersistencia {
		final ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentExecutionYear persistentExecutionYear = persistenceSupport.getIPersistentExecutionYear();

		final IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

		if (executionYear.getIdInternal() == null) {
			throw new RuntimeException("Problems accesing fenix database!");
		}
	}

}
