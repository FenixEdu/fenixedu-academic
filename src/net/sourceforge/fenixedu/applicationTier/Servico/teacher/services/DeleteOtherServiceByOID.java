/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteOtherServiceByOID extends Service {

    public void run(Integer otherServiceID) throws ExcepcaoPersistencia {
        OtherService otherService = (OtherService) persistentSupport.getIPersistentOtherService()
                .readByOID(OtherService.class, otherServiceID);
        otherService.delete();
    }
}
