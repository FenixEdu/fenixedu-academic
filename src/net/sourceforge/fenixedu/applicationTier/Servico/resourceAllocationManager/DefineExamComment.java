package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DefineExamComment extends Service {

    public void run(String executionCourseInitials, Integer executionPeriodId, String comment) throws FenixServiceException,
	    ExcepcaoPersistencia {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	final ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseInitials);

	if (executionCourse == null) {
	    throw new FenixServiceException("error.noExecutionCourse");
	}
	executionCourse.setComment(comment);
    }

}
