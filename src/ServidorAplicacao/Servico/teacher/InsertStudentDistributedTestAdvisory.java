/*
 * Created on 19/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Advisory;
import Dominio.IAdvisory;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class InsertStudentDistributedTestAdvisory implements IService {

    public InsertStudentDistributedTestAdvisory() {
    }

    public void run(Integer executionCourseId, Integer advisoryId, Integer studentId)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IAdvisory advisory = (IAdvisory) persistentSuport.getIPersistentAdvisory().readByOID(
                    Advisory.class, advisoryId);
            IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(
                    Student.class, studentId);

            persistentSuport.getIPersistentAdvisory().write(advisory, student.getPerson());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
    }
}