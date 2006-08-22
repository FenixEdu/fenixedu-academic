package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;

public class WriteStudentAttendingCourse extends Service {

	public void run(Registration registration, Integer executionCourseId) throws FenixServiceException {

		if (registration == null) {
			throw new FenixServiceException("error.invalid.student");
		}
		registration.addAttendsTo(readExecutionCourse(executionCourseId));
	}

	private ExecutionCourse readExecutionCourse(Integer executionCourseId) throws FenixServiceException {
		final ExecutionCourse executionCourse = rootDomainObject
				.readExecutionCourseByOID(executionCourseId);
		if (executionCourse == null) {
			throw new FenixServiceException("noExecutionCourse");
		}
		return executionCourse;
	}
}