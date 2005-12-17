/*
 * Created on 13/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class UnEnrollStudentFromShift implements IService {

	/**
	 * 
	 */
	public UnEnrollStudentFromShift() {
	}

	/**
	 * @param studentId
	 * @param shiftId
	 * @return
	 * @throws StudentNotFoundServiceException
	 * @throws FenixServiceException
	 * @throws ExcepcaoPersistencia
	 */
	public Boolean run(Integer studentId, Integer shiftId)
			throws StudentNotFoundServiceException,
			ShiftNotFoundServiceException,
			ShiftEnrolmentNotFoundServiceException, FenixServiceException,
			ExcepcaoPersistencia {
		ISuportePersistente persistentSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();

		IShift shift = (IShift) persistentSupport.getITurnoPersistente()
				.readByOID(Shift.class, shiftId);
		if (shift == null) {
			throw new ShiftNotFoundServiceException();
		}

		IStudent student = (IStudent) persistentSupport.getIPersistentStudent()
				.readByOID(Student.class, studentId);
		if (student == null) {
			throw new StudentNotFoundServiceException();
		}

		if (student.getPayedTuition() == null
				|| student.getPayedTuition().equals(Boolean.FALSE)) {
			throw new FenixServiceException(
					"error.exception.notAuthorized.student.warningTuition");
		}

		shift.getStudents().remove(student);

		return new Boolean(true);
	}

	public class StudentNotFoundServiceException extends FenixServiceException {
	}

	public class ShiftNotFoundServiceException extends FenixServiceException {
	}

	public class ShiftEnrolmentNotFoundServiceException extends
			FenixServiceException {
	}

}