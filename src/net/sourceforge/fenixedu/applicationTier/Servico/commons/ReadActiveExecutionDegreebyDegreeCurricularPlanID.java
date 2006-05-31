package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID extends Service {

    public InfoExecutionDegree run(final Integer degreeCurricularPlanID) throws FenixServiceException,
            ExcepcaoPersistencia {

        // degree curricular plan
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
        if (executionDegree != null) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus.newInfoFromDomain(executionDegree);

            if (executionDegree.hasAnyCoordinatorsList()) {
                final List<InfoCoordinator> infoCoordinatorList = new ArrayList<InfoCoordinator>();
                for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                    infoCoordinatorList.add(InfoCoordinatorWithInfoPerson.newInfoFromDomain(coordinator));
                }

                infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
            }

            infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodExamsFirstSemester()));
            infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodExamsSecondSemester()));
            infoExecutionDegree.setInfoPeriodLessonsFirstSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodLessonsFirstSemester()));
            infoExecutionDegree.setInfoPeriodLessonsSecondSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodLessonsSecondSemester()));

            return infoExecutionDegree;
        }

        return null;
    }

}
