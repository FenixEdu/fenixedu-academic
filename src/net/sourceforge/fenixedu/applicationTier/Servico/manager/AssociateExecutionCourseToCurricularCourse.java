package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class AssociateExecutionCourseToCurricularCourse extends Service {

    public void run(Integer executionCourseId, Integer curricularCourseId, Integer executionPeriodId)
	    throws FenixServiceException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
	if (curricularCourse == null) {
	    throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
	}

	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	if (executionSemester == null) {
	    throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
	}

	List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
	for (ExecutionCourse executionCourse : executionCourses) {
	    if (executionCourse.getExecutionPeriod() == executionSemester) {
		throw new ExistingServiceException("message.unavailable.execution.period", null);
	    }
	}

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null) {
	    throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
	}

	if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
	    curricularCourse.addAssociatedExecutionCourses(executionCourse);
	}
    }

}
