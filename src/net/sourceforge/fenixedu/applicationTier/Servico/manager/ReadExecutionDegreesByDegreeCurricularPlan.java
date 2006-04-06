package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionDegreesByDegreeCurricularPlan extends Service {

    public List<InfoExecutionDegree> run(Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(idDegreeCurricularPlan);

        List<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (ExecutionDegree executionDegree : executionDegrees) {
            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYear.newInfoFromDomain(executionDegree);

            if (executionDegree.hasAnyCoordinatorsList()) {
                List<InfoCoordinator> infoCoordinators = buildInfoCoordinators(executionDegree.getCoordinatorsList());
                infoExecutionDegree.setCoordinatorsList(infoCoordinators);
            }

            if (executionDegree.hasPeriodExamsFirstSemester()) {
                infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodExamsFirstSemester()));
            }
            if (executionDegree.hasPeriodExamsSecondSemester()) {
                infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodExamsSecondSemester()));
            }
            if (executionDegree.hasPeriodLessonsFirstSemester()) {
                infoExecutionDegree.setInfoPeriodLessonsFirstSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodLessonsFirstSemester()));
            }
            if (executionDegree.hasPeriodLessonsSecondSemester()) {
                infoExecutionDegree.setInfoPeriodLessonsSecondSemester(InfoPeriod.newInfoFromDomain(executionDegree.getPeriodLessonsSecondSemester()));
            }

            result.add(infoExecutionDegree);
        }

        return result;
    }

    private List<InfoCoordinator> buildInfoCoordinators(List<Coordinator> coordinators) {
        List<InfoCoordinator> infoCoordinators = new ArrayList<InfoCoordinator>();
        for (Coordinator coordinator : coordinators) {
            infoCoordinators.add(InfoCoordinatorWithInfoPerson.newInfoFromDomain(coordinator));
        }
        return infoCoordinators;
    }
    
}
