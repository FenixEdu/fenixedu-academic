package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;

public class UnEnrollStudentFromShift extends Service {

	public void run(final Student student, final Integer shiftId)
			throws StudentNotFoundServiceException, ShiftNotFoundServiceException,
			ShiftEnrolmentNotFoundServiceException, FenixServiceException {

		if (student == null) {
			throw new StudentNotFoundServiceException();
		}
		if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
			throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
		}

		final Shift shift = rootDomainObject.readShiftByOID(shiftId);
		if (shift == null) {
			throw new ShiftNotFoundServiceException();
		}
		
		shift.removeStudents(student);
	}

	public class StudentNotFoundServiceException extends FenixServiceException {
	}

	public class ShiftNotFoundServiceException extends FenixServiceException {
	}

	public class ShiftEnrolmentNotFoundServiceException extends FenixServiceException {
	}

}
