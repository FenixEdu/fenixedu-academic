package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithCoordinators;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Tânia Pousão Create on 13/Nov/2003
 */
public class ReadExecutionDegreesByDegreeAndExecutionPeriod extends Service {

	public List run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException, ExcepcaoPersistencia {
		List infoExecutionDegreeList = null;

		if (degreeId == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

		// Execution OccupationPeriod
		ExecutionPeriod executionPeriod;
		if (executionPeriodId == null) {
			executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
		} else {

			executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
					ExecutionPeriod.class, executionPeriodId);
		}

		if (executionPeriod == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		ExecutionYear executionYear = executionPeriod.getExecutionYear();
		if (executionYear == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Degree
		ICursoPersistente persistentDegree = sp.getICursoPersistente();
		Degree degree = (Degree) persistentDegree.readByOID(Degree.class, degreeId);
		if (degree == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Execution degrees
		IPersistentExecutionDegree persistentExecutionDegre = sp.getIPersistentExecutionDegree();
		List executionDegreeList = persistentExecutionDegre.readByDegreeAndExecutionYear(degree
				.getIdInternal(), executionYear.getYear(), CurricularStage.OLD);
		if (executionDegreeList == null || executionDegreeList.size() <= 0) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		infoExecutionDegreeList = new ArrayList();
		ListIterator listIterator = executionDegreeList.listIterator();
		while (listIterator.hasNext()) {
			ExecutionDegree executionDegree = (ExecutionDegree) listIterator.next();

			InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithCoordinators
					.newInfoFromDomain(executionDegree);
			InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionDegree
					.getExecutionYear());
			infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
					.newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
			InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree
					.getDegreeCurricularPlan().getDegree());
			infoDegreeCurricularPlan.setInfoDegree(infoDegree);
			infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

			infoExecutionDegreeList.add(infoExecutionDegree);
		}

		return infoExecutionDegreeList;
	}
}