/*
 * Created on 21/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class EditStudentGroupShift implements IService {

    /**
     * The constructor of this class.
     */
    public EditStudentGroupShift() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer newShiftCode)
            throws FenixServiceException {

        ITurnoPersistente persistentShift = null;
        IPersistentStudentGroup persistentStudentGroup = null;

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            persistentShift = persistentSupport.getITurnoPersistente();
            ITurno shift = (ITurno) persistentShift.readByOID(Turno.class, newShiftCode);

            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                    StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new InvalidArgumentsServiceException();
            }

            persistentStudentGroup.simpleLockWrite(studentGroup);
            studentGroup.setShift(shift);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}