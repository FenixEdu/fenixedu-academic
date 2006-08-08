package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;

public class WriteStudentAttendingCourse extends Service {

	public void run(Student student, Integer executionCourseId) throws FenixServiceException {

		if (student == null) {
			throw new FenixServiceException("error.invalid.student");
		}
		student.addAttendsTo(readExecutionCourse(executionCourseId));
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