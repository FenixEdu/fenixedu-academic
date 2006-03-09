/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesOfDegreeCurricularPlan extends Service {

	// delete a set of executionDegrees
	public List run(List executionDegreesIds) throws FenixServiceException, ExcepcaoPersistencia {
		ITurmaPersistente persistentClass = persistentSupport.getITurmaPersistente();

		Iterator iter = executionDegreesIds.iterator();

		List undeletedExecutionDegreesYears = new ArrayList();
		List classes;
		Integer executionDegreeId;
		ExecutionDegree executionDegree;

		List masterDegreeCandidates;
		List guides;
		List proposals;
		List groups;
		Scheduleing scheduleing;

		while (iter.hasNext()) {

			executionDegreeId = (Integer) iter.next();

			executionDegree = (ExecutionDegree) persistentObject.readByOID(
					ExecutionDegree.class, executionDegreeId);
			if (executionDegree != null) {
				classes = persistentClass.readByExecutionDegree(executionDegree.getIdInternal());

				masterDegreeCandidates = executionDegree.getMasterDegreeCandidates();
				guides = executionDegree.getGuides();
				proposals = executionDegree.getScheduling().getProposals();
				groups = executionDegree.getAssociatedFinalDegreeWorkGroups();
				scheduleing = executionDegree.getScheduling();

				if (classes.isEmpty()
						&& masterDegreeCandidates.isEmpty()
						&& guides.isEmpty()
						&& proposals.isEmpty()
						&& groups.isEmpty()
						&& (scheduleing == null)
						&& executionDegree.getPeriodLessonsFirstSemester().getRoomOccupations()
								.isEmpty()
						&& executionDegree.getPeriodLessonsSecondSemester().getRoomOccupations()
								.isEmpty()
						&& executionDegree.getPeriodExamsFirstSemester().getRoomOccupations().isEmpty()
						&& executionDegree.getPeriodExamsSecondSemester().getRoomOccupations().isEmpty()) {

					// persistentExecutionDegree.delete(executionDegree);

					// ExecutionYear
					executionDegree.getExecutionYear().getExecutionDegrees().remove(executionDegree);
					executionDegree.setExecutionYear(null);

					// GratuityValues
					if (executionDegree.getGratuityValues() != null) {
						persistentSupport.getIPersistentGratuityValues().deleteByOID(GratuityValues.class,
								executionDegree.getGratuityValues().getIdInternal());
					}

					// PERIOD's
					executionDegree.getPeriodLessonsFirstSemester()
							.getExecutionDegreesForLessonsFirstSemester().remove(executionDegree);
					executionDegree.setPeriodLessonsFirstSemester(null);
					if ((executionDegree.getPeriodLessonsFirstSemester() != null)
							&& (executionDegree.getPeriodLessonsFirstSemester()
									.getExecutionDegreesForLessonsFirstSemester().size() == 1)) {
						persistentSupport.getIPersistentPeriod().deleteByOID(OccupationPeriod.class,
								executionDegree.getPeriodLessonsFirstSemester().getIdInternal());
					}

					executionDegree.getPeriodLessonsSecondSemester()
							.getExecutionDegreesForLessonsSecondSemester().remove(executionDegree);
					executionDegree.setPeriodLessonsSecondSemester(null);
					if ((executionDegree.getPeriodLessonsSecondSemester() != null)
							&& (executionDegree.getPeriodLessonsSecondSemester()
									.getExecutionDegreesForLessonsSecondSemester().size() == 1)) {
						persistentSupport.getIPersistentPeriod().deleteByOID(OccupationPeriod.class,
								executionDegree.getPeriodLessonsSecondSemester().getIdInternal());
					}

					executionDegree.getPeriodExamsFirstSemester()
							.getExecutionDegreesForExamsFirstSemester().remove(executionDegree);
					executionDegree.setPeriodExamsFirstSemester(null);
					if ((executionDegree.getPeriodExamsFirstSemester() != null)
							&& (executionDegree.getPeriodExamsFirstSemester()
									.getExecutionDegreesForExamsFirstSemester().size() == 1)) {
						persistentSupport.getIPersistentPeriod().deleteByOID(OccupationPeriod.class,
								executionDegree.getPeriodExamsFirstSemester().getIdInternal());
					}

					executionDegree.getPeriodExamsSecondSemester()
							.getExecutionDegreesForExamsSecondSemester().remove(executionDegree);
					executionDegree.setPeriodExamsSecondSemester(null);
					if ((executionDegree.getPeriodExamsSecondSemester() != null)
							&& (executionDegree.getPeriodExamsSecondSemester()
									.getExecutionDegreesForExamsSecondSemester().size() == 1)) {
						persistentSupport.getIPersistentPeriod().deleteByOID(OccupationPeriod.class,
								executionDegree.getPeriodExamsSecondSemester().getIdInternal());
					}

					// DegreeCurricularPlan
					executionDegree.getDegreeCurricularPlan().getExecutionDegrees().remove(
							executionDegree);
					executionDegree.setDegreeCurricularPlan(null);
					// Coordinator
					List<Coordinator> coordinators = executionDegree.getCoordinatorsList();
					for (Coordinator coordinator : coordinators) {
						coordinator.setExecutionDegree(null);
						persistentSupport.getIPersistentCoordinator().deleteByOID(Coordinator.class,
								coordinator.getIdInternal());
					}
					executionDegree.getCoordinatorsList().clear();
					// Campus
					executionDegree.getCampus().getExecutionDegrees().remove(executionDegree);
					executionDegree.setCampus(null);

					persistentSupport.getIPersistentExecutionDegree().deleteByOID(ExecutionDegree.class,
							executionDegree.getIdInternal());
				} else
					undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
			}
		}

		return undeletedExecutionDegreesYears;
	}
}