package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(executionDegree.getDegreeCurricularPlan());

        List allInfoCurricularCourses = new ArrayList();

        CollectionUtils.collect(allCurricularCourses, new Transformer() {
            public Object transform(Object arg0) {
                CurricularCourse curricularCourse = (CurricularCourse) arg0;
                List allInfoCurricularCourseScopes = new ArrayList();
                CollectionUtils.collect(curricularCourse.getScopes(), new Transformer() {
                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                        return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }
                }, allInfoCurricularCourseScopes);

                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                        .newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoScopes(allInfoCurricularCourseScopes);
                return infoCurricularCourse;
            }
        }, allInfoCurricularCourses);

        infoDegreeCurricularPlan.setCurricularCourses(allInfoCurricularCourses);
        return infoDegreeCurricularPlan;
    }
}
