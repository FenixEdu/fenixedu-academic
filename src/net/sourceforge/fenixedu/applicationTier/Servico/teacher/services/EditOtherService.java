/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditOtherService implements IService {

    public void run(Integer otherServiceID, Double credits, String reason) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        OtherService otherService = (OtherService) persistentSupport.getIPersistentOtherService()
                .readByOID(OtherService.class, otherServiceID);
        otherService.setCredits(credits);
        otherService.setReason(reason);
    }
}
