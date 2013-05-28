package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;


import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class WriteStudentAttendingCourse {

    protected void run(Registration registration, Integer executionCourseId) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, executionCourseId);

        if (registration == null) {
            throw new FenixServiceException("error.invalid.student");
        }
        registration.addAttendsTo(readExecutionCourse(executionCourseId));
    }

    private ExecutionCourse readExecutionCourse(Integer executionCourseId) throws FenixServiceException {
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("noExecutionCourse");
        }
        return executionCourse;
    }

    // Service Invokers migrated from Berserk

    private static final WriteStudentAttendingCourse serviceInstance = new WriteStudentAttendingCourse();

    @Service
    public static void runWriteStudentAttendingCourse(Registration registration, Integer executionCourseId)
            throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        serviceInstance.run(registration, executionCourseId);
    }

}