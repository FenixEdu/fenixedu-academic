/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesOfDegreeCurricularPlan implements IService {

    // delete a set of executionDegrees
    public List run(List executionDegreesIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            ITurmaPersistente persistentClass = sp.getITurmaPersistente();

            Iterator iter = executionDegreesIds.iterator();

            List undeletedExecutionDegreesYears = new ArrayList();
            List classes;
            Integer executionDegreeId;
            IExecutionDegree executionDegree;

            List masterDegreeCandidates;
            List guides;
            List proposals;
            List groups;
            IScheduleing scheduleing;

            while (iter.hasNext()) {

                executionDegreeId = (Integer) iter.next();

                executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                        ExecutionDegree.class, executionDegreeId);
                if (executionDegree != null) {
                    classes = persistentClass.readByExecutionDegree(executionDegree);

                    masterDegreeCandidates = executionDegree.getMasterDegreeCandidates();
                    guides = executionDegree.getGuides();
                    proposals = executionDegree.getAssociatedFinalDegreeWorkProposals();
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
                            && executionDegree.getPeriodExamsFirstSemester().getRoomOccupations()
                                    .isEmpty()
                            && executionDegree.getPeriodExamsSecondSemester().getRoomOccupations()
                                    .isEmpty()) {

                        // persistentExecutionDegree.delete(executionDegree);

                        // ExecutionYear
                        executionDegree.getExecutionYear().getExecutionDegrees().remove(executionDegree);
                        executionDegree.setExecutionYear(null);
                        // GratuityValues
                        List<IGratuityValues> gratuityValues = executionDegree.getGratuityValues();
                        for (IGratuityValues gratuityValue : gratuityValues) {
                            gratuityValue.setExecutionDegree(null);
                            sp.getIPersistentGratuityValues().deleteByOID(GratuityValues.class,
                                    gratuityValue.getIdInternal());
                        }
                        executionDegree.getGratuityValues().clear();
                        // PERIOD's
                        executionDegree.getPeriodLessonsFirstSemester()
                                .getExecutionDegreesForLessonsFirstSemester().remove(executionDegree);
                        executionDegree.setPeriodLessonsFirstSemester(null);
                        if (executionDegree.getPeriodLessonsFirstSemester()
                                .getExecutionDegreesForLessonsFirstSemester().size() == 1) {
                            sp.getIPersistentPeriod().deleteByOID(Period.class,
                                    executionDegree.getPeriodLessonsFirstSemester().getIdInternal());
                        }
                        
                        executionDegree.getPeriodLessonsSecondSemester()
                                .getExecutionDegreesForLessonsSecondSemester().remove(executionDegree);
                        executionDegree.setPeriodLessonsSecondSemester(null);
                        if (executionDegree.getPeriodLessonsSecondSemester()
                                .getExecutionDegreesForLessonsSecondSemester().size() == 1) {
                            sp.getIPersistentPeriod().deleteByOID(Period.class,
                                    executionDegree.getPeriodLessonsSecondSemester().getIdInternal());
                        }
                        
                        executionDegree.getPeriodExamsFirstSemester()
                                .getExecutionDegreesForExamsFirstSemester().remove(executionDegree);
                        executionDegree.setPeriodExamsFirstSemester(null);
                        if (executionDegree.getPeriodExamsFirstSemester()
                                .getExecutionDegreesForExamsFirstSemester().size() == 1) {
                            sp.getIPersistentPeriod().deleteByOID(Period.class,
                                    executionDegree.getPeriodExamsFirstSemester().getIdInternal());
                        }
                        
                        executionDegree.getPeriodExamsSecondSemester()
                                .getExecutionDegreesForExamsSecondSemester().remove(executionDegree);
                        executionDegree.setPeriodExamsSecondSemester(null);
                        if (executionDegree.getPeriodExamsSecondSemester()
                                .getExecutionDegreesForExamsSecondSemester().size() == 1) {
                            sp.getIPersistentPeriod().deleteByOID(Period.class,
                                    executionDegree.getPeriodExamsSecondSemester().getIdInternal());
                        }
                        
                        // DegreeCurricularPlan
                        executionDegree.getDegreeCurricularPlan().getExecutionDegrees().remove(
                                executionDegree);
                        executionDegree.setDegreeCurricularPlan(null);
                        // Coordinator
                        List<ICoordinator> coordinators = executionDegree.getCoordinatorsList();
                        for (ICoordinator coordinator : coordinators) {
                            coordinator.setExecutionDegree(null);
                            sp.getIPersistentCoordinator().deleteByOID(Coordinator.class,
                                    coordinator.getIdInternal());
                        }
                        executionDegree.getCoordinatorsList().clear();
                        // Campus
                        executionDegree.getCampus().getExecutionDegrees().remove(executionDegree);
                        executionDegree.setCampus(null);

                        sp.getIPersistentExecutionDegree().deleteByOID(ExecutionDegree.class,
                                executionDegree.getIdInternal());
                    } else
                        undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
                }
            }

            return undeletedExecutionDegreesYears;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}