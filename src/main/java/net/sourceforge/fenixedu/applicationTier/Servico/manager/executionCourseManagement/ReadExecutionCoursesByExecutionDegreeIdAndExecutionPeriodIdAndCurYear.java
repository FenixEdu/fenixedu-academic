package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */
public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear {

    @Service
    public static List run(String executionDegreeId, String executionPeriodId, Integer curricularYearInt)
            throws FenixServiceException {

        if (executionPeriodId == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }

        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodId);

        final List<ExecutionCourse> executionCourseList;
        if (executionDegreeId == null && curricularYearInt == null) {
            executionCourseList = executionSemester.getExecutionCoursesWithNoCurricularCourses();
        } else {
            final ExecutionDegree executionDegree = findExecutionDegreeByID(executionSemester, executionDegreeId);
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final CurricularYear curricularYear = CurricularYear.readByYear(curricularYearInt);
            executionCourseList =
                    executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                            degreeCurricularPlan, curricularYear, "%");
        }

        final List infoExecutionCourseList = new ArrayList(executionCourseList.size());
        for (final ExecutionCourse executionCourse : executionCourseList) {
            infoExecutionCourseList.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return infoExecutionCourseList;
    }

    private static ExecutionDegree findExecutionDegreeByID(final ExecutionSemester executionSemester,
            final String executionDegreeId) {
        final ExecutionYear executionYear = executionSemester.getExecutionYear();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            if (executionDegree.getExternalId().equals(executionDegreeId)) {
                return executionDegree;
            }
        }
        return null;
    }

}