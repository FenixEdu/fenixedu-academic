package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID implements IService {

    public InfoExecutionDegree run(final Integer degreeCurricularPlanID) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = suportePersistente
                .getIPersistentDegreeCurricularPlan();
        final IPersistentExecutionDegree persistentExecutionDegree = suportePersistente
                .getIPersistentExecutionDegree();

        // degree curricular plan
        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        // and correspondent execution degrees
        final List executionDegrees = persistentExecutionDegree
                .readByDegreeCurricularPlan(degreeCurricularPlan.getIdInternal());
        if (executionDegrees == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        // sort them by begin date
        Collections.sort(executionDegrees, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((IExecutionDegree) o1).getExecutionYear().getBeginDate().compareTo(
                        ((IExecutionDegree) o1).getExecutionYear().getBeginDate());
            }
        });

        // decide which is the execution year which we want to edit
        final IExecutionDegree executionDegree = getActiveExecutionYear(executionDegrees);
        if (executionDegree != null) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus
                    .newInfoFromDomain(executionDegree);

            if (executionDegree.getCoordinatorsList() != null) {
                final List infoCoordinatorList = new ArrayList();
                for (final Iterator iterator = executionDegree.getCoordinatorsList().iterator(); iterator
                        .hasNext();) {
                    final ICoordinator coordinator = (ICoordinator) iterator.next();
                    final InfoCoordinator infoCoordinator = new InfoCoordinator();
                    infoCoordinatorList.add(infoCoordinator);
                    infoCoordinator.setIdInternal(coordinator.getIdInternal());

                    final ITeacher teacher = coordinator.getTeacher();
                    final InfoTeacher infoTeacher = new InfoTeacher();
                    infoCoordinator.setInfoTeacher(infoTeacher);
                    infoTeacher.setIdInternal(teacher.getIdInternal());

                    final IPerson person = teacher.getPerson();
                    final InfoPerson infoPerson = new InfoPerson();
                    infoTeacher.setInfoPerson(infoPerson);
                    infoPerson.setIdInternal(person.getIdInternal());
                    infoPerson.setNome(person.getNome());
                }

                infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
            }

            infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsFirstSemester()));
            infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsSecondSemester()));
            infoExecutionDegree.setInfoPeriodLessonsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsFirstSemester()));
            infoExecutionDegree.setInfoPeriodLessonsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsSecondSemester()));

            return infoExecutionDegree;
        }

        return null;
    }

    /**
     * This method basicly returns the most active execution degree, counting
     * from the present day This means that a previous execution degree cannot
     * be chosen (because it has already finished), and that only the first
     * execution degree can be chosen, fom a curricular plan that has not yet
     * started
     * 
     * @param infoExecutionDegreeList
     *            a list of infoexecution degrees sorted by beginDate
     */

    private IExecutionDegree getActiveExecutionYear(List executionDegreeList) {
        Date todayDate = new Date();
        int i;
        for (i = 0; i < executionDegreeList.size(); i++) {
            // if the first is in the future, the rest is also
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
     * @return -1 if the InfoExecutionDegree has already finished 0 if it is
     *         active 1 if it is in the future
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