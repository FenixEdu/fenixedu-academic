package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EnrollStudentInShifts implements IService {

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    public ShiftEnrollmentErrorReport run(final Integer studentId, final Integer shiftId)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        final ITurnoPersistente persistentShift = sp.getITurnoPersistente();

        final ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

        final IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentId);
        if (student == null) {
            throw new StudentNotFoundServiceException();
        }

        if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        final IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftId);
        if (shift == null) {
            errorReport.getUnExistingShifts().add(shiftId);
        }
        
        //check if is a 1st year shift  
        if(CollectionUtils.exists(shift.getAssociatedClasses(), new Predicate() {

			public boolean evaluate(Object arg0) {
				ISchoolClass schoolClass = (ISchoolClass) arg0;
				if(schoolClass.getAnoCurricular().equals(1)) {
					return true;
				}
				return false;
			}
        	
        })) {
        	throw new FenixServiceException("error.cannot.reserve.first.year.shift");
        }

        final IShift shiftFromStudent = findShiftOfSameTypeForSameExecutionCourse(student, shift);
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

    private IShift findShiftOfSameTypeForSameExecutionCourse(final IStudent student, final IShift shift) {
        final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();

        final List<IShift> shiftsFromStudent = student.getShifts();
        for (final IShift shiftFromStudent : shiftsFromStudent) {
            if (shiftFromStudent.getTipo() == shift.getTipo()) {
                final IExecutionCourse executionCourseFromStudent = shiftFromStudent.getDisciplinaExecucao();
                if (executionCourseFromStudent == executionCourse) {
                    return shiftFromStudent;
                }
            }
        }

        return null;
    }

    private boolean reserveShiftForStudent(IStudent student, IShift shift) {
        final List<IStudent> students = shift.getStudents();
        if (shift.getLotacao().intValue() > students.size()) {
            students.add(student);
            return true;
        } else {
            return false;
        }
    }

}