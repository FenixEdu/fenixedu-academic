package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class DissociateExecutionCourse extends Service {

    public void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException {
	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
	if (curricularCourse == null) {
	    throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
	}

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null) {
	    throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
	}

	List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
	List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();

	if (!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
	    executionCourses.remove(executionCourse);
	    curricularCourses.remove(curricularCourse);
	}
    }

}
