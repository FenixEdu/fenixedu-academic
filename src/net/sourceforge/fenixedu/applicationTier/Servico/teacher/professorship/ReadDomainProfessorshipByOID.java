/**
* Nov 23, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadDomainProfessorshipByOID extends Service {

    public Professorship run(Integer professorshipID) throws ExcepcaoPersistencia {
        return (Professorship) persistentObject.readByOID(Professorship.class, professorshipID);
    }
}


