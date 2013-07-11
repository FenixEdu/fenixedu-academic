package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class WriteStudentAttendingCourse {

    protected void run(Registration registration, String executionCourseId) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, executionCourseId);

        if (registration == null) {
            throw new FenixServiceException("error.invalid.student");
        }
        registration.addAttendsTo(readExecutionCourse(executionCourseId));
    }

    private ExecutionCourse readExecutionCourse(String executionCourseId) throws FenixServiceException {
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("noExecutionCourse");
        }
        return executionCourse;
    }

    // Service Invokers migrated from Berserk

    private static final WriteStudentAttendingCourse serviceInstance = new WriteStudentAttendingCourse();

    @Atomic
    public static void runWriteStudentAttendingCourse(Registration registration, String executionCourseId)
            throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        serviceInstance.run(registration, executionCourseId);
    }

}