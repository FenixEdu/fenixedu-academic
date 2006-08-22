package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrollStudentInShifts extends Service {

    public class StudentNotFoundServiceException extends FenixServiceException {}

    public ShiftEnrollmentErrorReport run(final Registration registration, final Integer shiftId) throws FenixServiceException {
    	
        final ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

        if (registration == null) {
            throw new StudentNotFoundServiceException();
        }

        if (registration.getPayedTuition() == null || registration.getPayedTuition().equals(Boolean.FALSE)) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        final Shift selectedShift = rootDomainObject.readShiftByOID(shiftId);
        if (selectedShift == null) {
            errorReport.getUnExistingShifts().add(shiftId);
        }             

        final Shift shiftFromStudent = findShiftOfSameTypeForSameExecutionCourse(registration, selectedShift);
        
        if (selectedShift != shiftFromStudent) {
            //Registration is not yet enroled, so let's reserve the shift...
            if (selectedShift.reserveForStudent(registration)) {
                if (shiftFromStudent != null) {
                    shiftFromStudent.removeStudents(registration);
                }
            } else {
                errorReport.getUnAvailableShifts().add(selectedShift);
            }
        }

        return errorReport;
    }

    private Shift findShiftOfSameTypeForSameExecutionCourse(final Registration registration, final Shift shift) {
		for (final Shift shiftFromStudent : registration.getShifts()) {
			if (shiftFromStudent.getTipo() == shift.getTipo()
					&& shiftFromStudent.getDisciplinaExecucao() == shift.getDisciplinaExecucao()) {
				return shiftFromStudent;
			}
		}
		return null;
	}    

}