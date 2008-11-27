/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;

public class DisassociateParentUnit extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer accountabilityID) throws FenixServiceException {
	Accountability accountability = rootDomainObject.readAccountabilityByOID(accountabilityID);
	if (accountability == null) {
	    throw new FenixServiceException("error.inexistent.accountability");
	}
	accountability.delete();
    }
}