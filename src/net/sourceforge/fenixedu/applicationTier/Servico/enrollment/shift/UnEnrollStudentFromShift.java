/*
 * Created on 13/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftStudent;
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
     * @return @throws
     *         StudentNotFoundServiceException
     * @throws FenixServiceException
     */
    public Boolean run(Integer studentId, Integer shiftId) throws StudentNotFoundServiceException,
            ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException, FenixServiceException {
        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IShift shift = (IShift) persistentSupport.getITurnoPersistente().readByOID(Shift.class,
                    shiftId);
            if (shift == null) {
                throw new ShiftNotFoundServiceException();
            }

            IStudent student = (IStudent) persistentSupport.getIPersistentStudent().readByOID(
                    Student.class, studentId);
            if (student == null) {
                throw new StudentNotFoundServiceException();
            }

            if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
                throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
            }

            IShiftStudent studentShift = persistentSupport.getITurnoAlunoPersistente()
                    .readByTurnoAndAluno(shift.getIdInternal(), student.getIdInternal());
            if (studentShift == null) {
                throw new ShiftEnrolmentNotFoundServiceException();
            }

            //persistentSupport.getITurnoAlunoPersistente().delete(studentShift);
            studentShift.getStudent().getShiftStudents().remove(studentShift);
            studentShift.getShift().getStudentShifts().remove(studentShift);
            studentShift.setShift(null);
            studentShift.setStudent(null);
            persistentSupport.getITurnoAlunoPersistente().deleteByOID(ShiftStudent.class, studentShift.getIdInternal());
            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    public class ShiftNotFoundServiceException extends FenixServiceException {
    }

    public class ShiftEnrolmentNotFoundServiceException extends FenixServiceException {
    }

}