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

public class EditOtherService extends Service {

    public void run(Integer otherServiceID, Double credits, String reason) {
	OtherService otherService = (OtherService) rootDomainObject.readTeacherServiceItemByOID(otherServiceID);
	otherService.setCredits(credits);
	otherService.setReason(reason);
    }

}
