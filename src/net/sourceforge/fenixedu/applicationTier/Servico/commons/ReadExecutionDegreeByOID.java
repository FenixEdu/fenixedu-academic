/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadExecutionDegreeByOID implements IService {

	public InfoExecutionDegree run(Integer oid) throws ExcepcaoPersistencia {

		InfoExecutionDegree infoExecutionDegree = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentObject persistentObject = sp.getIPersistentObject();
		ExecutionDegree executionDegree = (ExecutionDegree) persistentObject
				.readByOID(ExecutionDegree.class, oid);
		if (executionDegree != null) {

			infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus
					.newInfoFromDomain(executionDegree);

			if (executionDegree.getCoordinatorsList() != null) {
				List infoCoordinatorList = new ArrayList();
				ListIterator iteratorCoordinator = executionDegree
						.getCoordinatorsList().listIterator();
				while (iteratorCoordinator.hasNext()) {
					Coordinator coordinator = (Coordinator) iteratorCoordinator
							.next();

					infoCoordinatorList.add(InfoCoordinatorWithInfoPerson
							.newInfoFromDomain(coordinator));
				}

				infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
			}

			if (executionDegree.getPeriodExamsFirstSemester() != null) {
				infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod
						.newInfoFromDomain(executionDegree
								.getPeriodExamsFirstSemester()));
			}
			if (executionDegree.getPeriodExamsSecondSemester() != null) {
				infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod
						.newInfoFromDomain(executionDegree
								.getPeriodExamsSecondSemester()));
			}
			if (executionDegree.getPeriodLessonsFirstSemester() != null) {
				infoExecutionDegree
						.setInfoPeriodLessonsFirstSemester(InfoPeriod
								.newInfoFromDomain(executionDegree
										.getPeriodLessonsFirstSemester()));
			}
			if (executionDegree.getPeriodLessonsSecondSemester() != null) {
				infoExecutionDegree
						.setInfoPeriodLessonsSecondSemester(InfoPeriod
								.newInfoFromDomain(executionDegree
										.getPeriodLessonsSecondSemester()));
			}

		}

		return infoExecutionDegree;
	}
}