
package ServidorAplicacao.Servico.student;

/**
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentByUsername implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentByUsername() {
    }

    public Object run(String username) throws FenixServiceException {

        InfoStudent infoStudent = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudent student = sp.getIPersistentStudent().readByUsername(username);

            if (student != null) {
                infoStudent = InfoStudent.newInfoFromDomain(student);

            }
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex);
        }

        return infoStudent;
    }
}