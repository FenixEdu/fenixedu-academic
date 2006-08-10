package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DeleteStudentAttendingCourse extends Service {

    public void run(Registration student, Integer executionCourseID) throws FenixServiceException {
    	if (student == null) {
            throw new FenixServiceException("error.exception.noStudents");
        }
    	student.removeAttendFor(readExecutionCourse(executionCourseID));
    }

    private ExecutionCourse readExecutionCourse(final Integer executionCourseID) throws FenixServiceException {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        return executionCourse;
    }
}
