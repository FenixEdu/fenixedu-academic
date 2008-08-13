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

    public void run(Integer otherServiceID) {
        OtherService otherService = (OtherService) rootDomainObject.readTeacherServiceItemByOID(otherServiceID);
        otherService.delete();
    }
    
}
