/*
 * Created on 13/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.enrollment.shift;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.IShift;
import Dominio.ITurnoAluno;
import Dominio.Student;
import Dominio.Shift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

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

            ITurnoAluno studentShift = persistentSupport.getITurnoAlunoPersistente()
                    .readByTurnoAndAluno(shift, student);
            if (studentShift == null) {
                throw new ShiftEnrolmentNotFoundServiceException();
            }

            persistentSupport.getITurnoAlunoPersistente().delete(studentShift);
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