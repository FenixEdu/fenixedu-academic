package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quit�rio 10/Nov/2003
 * 
 */
public class ReadDegreeCurricularPlanHistoryByExecutionDegreeCode extends Service {

    public InfoDegreeCurricularPlan run(Integer executionDegreeCode) throws FenixServiceException, ExcepcaoPersistencia {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        if (executionDegreeCode == null) {
            throw new FenixServiceException("nullDegree");
        }

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeCode);

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }
        DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        if (degreeCurricularPlan != null) {

            List<CurricularCourse> allCurricularCourses = degreeCurricularPlan.getCurricularCourses();

            if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {

                infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(executionDegree,
                        allCurricularCourses);
            }
        }

        return infoDegreeCurricularPlan;
    }

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(ExecutionDegree executionDegree,
            List allCurricularCourses) {
        return InfoDegreeCurricularPlan.newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
    }

}
