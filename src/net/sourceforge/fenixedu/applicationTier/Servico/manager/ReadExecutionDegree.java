/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class ReadExecutionDegree extends Service {

    /**
     * Executes the service. Returns the current InfoExecutionDegree.
     * 
     * @throws ExcepcaoPersistencia
     */
    public InfoExecutionDegree run(Integer idInternal) throws FenixServiceException,
            ExcepcaoPersistencia {
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(idInternal);

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);

        final ExecutionYear executionYear = executionDegree.getExecutionYear();
        final InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

        final Campus campus = executionDegree.getCampus();
        final InfoCampus infoCampus = InfoCampus.newInfoFromDomain(campus);
        infoExecutionDegree.setInfoCampus(infoCampus);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        // added by Tânia Pousão
        if (executionDegree.getCoordinatorsList() != null) {
            List infoCoordinatorList = new ArrayList();
            ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList().listIterator();
            while (iteratorCoordinator.hasNext()) {
                Coordinator coordinator = (Coordinator) iteratorCoordinator.next();

                InfoCoordinator infoCoordinator = InfoCoordinator.newInfoFromDomain(coordinator);
                infoCoordinatorList.add(infoCoordinator);

                final Teacher teacher = coordinator.getTeacher();
                final InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                infoCoordinator.setInfoTeacher(infoTeacher);
            }

            infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
        }

        if (executionDegree.getPeriodExamsFirstSemester() != null) {
            infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsFirstSemester()));
        }
        if (executionDegree.getPeriodExamsSecondSemester() != null) {
            infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsSecondSemester()));
        }
        if (executionDegree.getPeriodLessonsFirstSemester() != null) {
            infoExecutionDegree.setInfoPeriodLessonsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsFirstSemester()));
        }
        if (executionDegree.getPeriodLessonsSecondSemester() != null) {
            infoExecutionDegree.setInfoPeriodLessonsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsSecondSemester()));
        }

        return infoExecutionDegree;
    }
}