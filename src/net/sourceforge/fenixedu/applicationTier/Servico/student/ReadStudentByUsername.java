package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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