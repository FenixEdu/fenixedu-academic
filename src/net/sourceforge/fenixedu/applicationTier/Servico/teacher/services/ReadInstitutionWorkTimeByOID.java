/**
* Nov 24, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadInstitutionWorkTimeByOID extends Service {

    public InstitutionWorkTime run(Integer institutionWorkTimeID) throws ExcepcaoPersistencia{
        
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        return (InstitutionWorkTime) persistentSupport.getIPersistentInstitutionWorkTime().readByOID(
                InstitutionWorkTime.class, institutionWorkTimeID);
    }
}


