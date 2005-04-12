package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID implements IService {

    public ReadActiveExecutionDegreebyDegreeCurricularPlanID() {

    }

    public InfoExecutionDegree run(Integer degreeCurricularPlanID) throws FenixServiceException {

        InfoExecutionDegree infoExecutionDegree = null;
        List executionDegrees = null;
        try {
            ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // degree curricular plan
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) suportePersistente
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            if (degreeCurricularPlan == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            // and correspondent execution degrees
            executionDegrees = suportePersistente.getIPersistentExecutionDegree()
                    .readByDegreeCurricularPlan(degreeCurricularPlan);

            // sort them by begin date

            Collections.sort(executionDegrees, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((IExecutionDegree) o1).getExecutionYear().getBeginDate().compareTo(
                            ((IExecutionDegree) o1).getExecutionYear().getBeginDate());
                }
            });

            if (executionDegrees == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            // decide which is the execution year which we want to edit
            IExecutionDegree executionDegree = getActiveExecutionYear(executionDegrees);
            if (executionDegree != null) {

                infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus
                        .newInfoFromDomain(executionDegree);

                if (executionDegree.getCoordinatorsList() != null) {
                    List infoCoordinatorList = new ArrayList();
                    ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList()
                            .listIterator();
                    while (iteratorCoordinator.hasNext()) {
                        ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();

                        infoCoordinatorList.add(InfoCoordinatorWithInfoPerson
                                .newInfoFromDomain(coordinator));
                    }

                    infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
                }

                if (executionDegree.getPeriodExamsFirstSemester() != null) {
                    infoExecutionDegree.setInfoPeriodExamsFirstSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsFirstSemester()));
                }
                if (executionDegree.getPeriodExamsSecondSemester() != null) {
                    infoExecutionDegree.setInfoPeriodExamsSecondSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsSecondSemester()));
                }
                if (executionDegree.getPeriodLessonsFirstSemester() != null) {
                    infoExecutionDegree.setInfoPeriodLessonsFirstSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsFirstSemester()));
                }
                if (executionDegree.getPeriodLessonsSecondSemester() != null) {
                    infoExecutionDegree.setInfoPeriodLessonsSecondSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsSecondSemester()));
                }

            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoExecutionDegree;
    }

    /**
     * This method basicly returns the most active execution degree, counting
     * from the present day
     * This means that a previous execution degree cannot be chosen (because it has
     * already finished), and that only the first execution degree can be chosen,
     * fom a curricular plan that has not yet started
     * @param infoExecutionDegreeList a list of infoexecution degrees sorted by beginDate
     */

    private IExecutionDegree getActiveExecutionYear(List executionDegreeList) {
        Date todayDate = new Date();
        int i;
        for (i = 0; i < executionDegreeList.size(); i++) {
            //if the first is in the future, the rest is also
            if ((ExecutionDegreeDuringDate(todayDate, (IExecutionDegree) executionDegreeList.get(i)) == 1)
                    && (i == 0))
                return (IExecutionDegree) executionDegreeList.get(i);
            // if the last is in the past, there is no editable executionDegree
            if ((ExecutionDegreeDuringDate(todayDate, (IExecutionDegree) executionDegreeList.get(i)) == 1)
                    && (i == executionDegreeList.size() - 1))
                return null;

            if (ExecutionDegreeDuringDate(todayDate, (IExecutionDegree) executionDegreeList.get(i)) == 0)
                return (IExecutionDegree) executionDegreeList.get(i);
        }
        return null;
    }

    /**
     * @return
     * 			-1 if the InfoExecutionDegree has already finished
     * 			0 if it is active
     * 			1 if it is in the future
     */

    private int ExecutionDegreeDuringDate(Date date, IExecutionDegree info) {
        if (date.after(info.getExecutionYear().getEndDate()))
            return -1;
        if ((date.after(info.getExecutionYear().getBeginDate()))
                && date.before(info.getExecutionYear().getEndDate()))
            return 0;
        return 1;
    }

}