package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */
public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear extends Service {

    public Object run(Integer executionDegreeId, Integer executionPeriodId, Integer curricularYearInt)
	    throws FenixServiceException, ExcepcaoPersistencia {

	if (executionPeriodId == null) {
	    throw new FenixServiceException("nullExecutionPeriodId");
	}

	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	final List<ExecutionCourse> executionCourseList;
	if (executionDegreeId == null && curricularYearInt == null) {
	    executionCourseList = executionSemester.getExecutionCoursesWithNoCurricularCourses();
	} else {
	    final ExecutionDegree executionDegree = findExecutionDegreeByID(executionSemester, executionDegreeId);
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	    final CurricularYear curricularYear = CurricularYear.readByYear(curricularYearInt);
	    executionCourseList = executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
		    degreeCurricularPlan, curricularYear, "%");
	}

	final List infoExecutionCourseList = new ArrayList(executionCourseList.size());
	for (final ExecutionCourse executionCourse : executionCourseList) {
	    infoExecutionCourseList.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
	}

	return infoExecutionCourseList;
    }

    private ExecutionDegree findExecutionDegreeByID(final ExecutionSemester executionSemester, final Integer executionDegreeId) {
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
	    if (executionDegree.getIdInternal().equals(executionDegreeId)) {
		return executionDegree;
	    }
	}
	return null;
    }

}