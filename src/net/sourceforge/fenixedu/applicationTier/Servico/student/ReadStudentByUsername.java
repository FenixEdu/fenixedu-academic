package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentByUsername implements IService {

    public Object run(String username) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IStudent student = sp.getIPersistentStudent().readByUsername(username);

        if (student != null) {
            return InfoStudent.newInfoFromDomain(student);
        }

        return null;
    }
}