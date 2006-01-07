/**
* Nov 23, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadDomainProfessorshipByOID implements IService {

    public Professorship run(Integer professorshipID) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        return (Professorship) persistentSupport.getIPersistentProfessorship().readByOID(
                Professorship.class, professorshipID);
    }
}


