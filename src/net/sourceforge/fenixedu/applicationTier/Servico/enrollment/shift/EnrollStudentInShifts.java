package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EnrollStudentInShifts extends Service {

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    public ShiftEnrollmentErrorReport run(final Integer studentId, final Integer shiftId)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

        final Student student = (Student) persistentObject.readByOID(Student.class, studentId);
        if (student == null) {
            throw new StudentNotFoundServiceException();
        }

        if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        final Shift shift = (Shift) persistentObject.readByOID(Shift.class, shiftId);
        if (shift == null) {
            errorReport.getUnExistingShifts().add(shiftId);
        }             

        final Shift shiftFromStudent = findShiftOfSameTypeForSameExecutionCourse(student, shift);
        if (shift != shiftFromStudent) {
            //Student is not yet enroled, so let's reserve the shift...
            if (reserveShiftForStudent(student, shift)) {
                if (shiftFromStudent != null) {
                    shiftFromStudent.removeStudents(student);
                }
            } else {
                errorReport.getUnAvailableShifts().add(InfoShift.newInfoFromDomain(shift));
            }
        }

        return errorReport;
    }

    private Shift findShiftOfSameTypeForSameExecutionCourse(final Student student, final Shift shift) {
        final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();

        final List<Shift> shiftsFromStudent = student.getShifts();
        for (final Shift shiftFromStudent : shiftsFromStudent) {
            if (shiftFromStudent.getTipo() == shift.getTipo()) {
                final ExecutionCourse executionCourseFromStudent = shiftFromStudent.getDisciplinaExecucao();
                if (executionCourseFromStudent == executionCourse) {
                    return shiftFromStudent;
                }
            }
        }

        return null;
    }

    private boolean reserveShiftForStudent(Student student, Shift shift) {
        final List<Student> students = shift.getStudents();
        if (shift.getLotacao().intValue() > students.size()) {
            students.add(student);
            return true;
        } else {
            return false;
        }
    }

}