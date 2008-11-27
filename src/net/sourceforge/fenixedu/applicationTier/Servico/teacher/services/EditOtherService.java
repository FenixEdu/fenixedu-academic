/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacher.OtherService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditOtherService extends FenixService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(Integer otherServiceID, Double credits, String reason) {
	OtherService otherService = (OtherService) rootDomainObject.readTeacherServiceItemByOID(otherServiceID);
	otherService.setCredits(credits);
	otherService.setReason(reason);
    }

}