package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/*
 * Created on Mar 8, 2004
 *
 */

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ReadExecutionDegreesOfTypeDegree extends Service {

	public List run() throws ExcepcaoPersistencia {
		List executionDegrees = null;
		List infoExecutionDegrees = new ArrayList();

		ISuportePersistente suportePersistente = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		executionDegrees = suportePersistente
				.getIPersistentExecutionDegree()
				.readExecutionDegreesOfTypeDegree();

		if (executionDegrees != null) {
			Iterator iterator = executionDegrees.iterator();
			while (iterator.hasNext()) {
                ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);

                infoExecutionDegrees.add(infoExecutionDegree);
			}
		}

		return infoExecutionDegrees;
	}
}