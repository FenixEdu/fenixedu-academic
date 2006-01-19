/*
 * Created on 10/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author lmac1
 */
public class ReadExecutionPeriod implements IService {

	public InfoExecutionPeriod run(Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp;
		InfoExecutionPeriod infoExecutionPeriod = null;
		ExecutionPeriod executionPeriod = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		executionPeriod = (ExecutionPeriod) sp.getIPersistentExecutionPeriod().readByOID(
				ExecutionPeriod.class, executionPeriodId);

		if (executionPeriod == null) {
			throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
		}

		infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
				.newInfoFromDomain(executionPeriod);
		return infoExecutionPeriod;
	}
}