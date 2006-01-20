/*
 * Created on 10/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */
public class ReadExecutionPeriod extends Service {

	public InfoExecutionPeriod run(Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
		InfoExecutionPeriod infoExecutionPeriod = null;
		ExecutionPeriod executionPeriod = null;

		executionPeriod = (ExecutionPeriod) persistentSupport.getIPersistentExecutionPeriod().readByOID(
				ExecutionPeriod.class, executionPeriodId);

		if (executionPeriod == null) {
			throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
		}

		infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
				.newInfoFromDomain(executionPeriod);
		return infoExecutionPeriod;
	}
}