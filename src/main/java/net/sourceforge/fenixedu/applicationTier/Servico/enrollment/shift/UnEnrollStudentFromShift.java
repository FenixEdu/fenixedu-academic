package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class UnEnrollStudentFromShift {

    protected void run(final Registration registration, final String shiftId) throws StudentNotFoundServiceException,
            ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException, FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, shiftId);

        if (registration == null) {
            throw new StudentNotFoundServiceException();
        }
        if (registration.getPayedTuition() == null || registration.getPayedTuition().equals(Boolean.FALSE)) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        final Shift shift = FenixFramework.getDomainObject(shiftId);
        if (shift == null) {
            throw new ShiftNotFoundServiceException();
        }

        shift.removeStudents(registration);
    }

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    public class ShiftNotFoundServiceException extends FenixServiceException {
    }

    public class ShiftEnrolmentNotFoundServiceException extends FenixServiceException {
    }

    // Service Invokers migrated from Berserk

    private static final UnEnrollStudentFromShift serviceInstance = new UnEnrollStudentFromShift();

    @Atomic
    public static void runUnEnrollStudentFromShift(Registration registration, String shiftId)
            throws StudentNotFoundServiceException, ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException,
            FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        serviceInstance.run(registration, shiftId);
    }

}