/*
 * Created on 18/07/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadPublicExecutionPeriods extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {

		List result = new ArrayList();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

		List executionPeriods = executionPeriodDAO.readPublic();

		if (executionPeriods != null) {
			for (int i = 0; i < executionPeriods.size(); i++) {
				result.add(InfoExecutionPeriodWithInfoExecutionYear
						.newInfoFromDomain((ExecutionPeriod) executionPeriods.get(i)));
			}
		}

		return result;
	}
}