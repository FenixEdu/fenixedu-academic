package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;

public class CreateAdHocEvaluation extends Service {

    public void run(Integer executionCourseID, String name, String description, GradeScale gradeScale)
	    throws FenixServiceException {

	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

	if (executionCourse == null) {
	    throw new FenixServiceException("error.noExecutionCourse");
	}

	new AdHocEvaluation(executionCourse, name, description, gradeScale);
    }
}
