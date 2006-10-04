/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;

public class DisassociateParentUnit extends Service {

    public void run(Integer accountabilityID) throws FenixServiceException {
	Accountability accountability = rootDomainObject.readAccountabilityByOID(accountabilityID);
	if (accountability == null) {
	    throw new FenixServiceException("error.inexistent.accountability");
	}
	accountability.delete();
    }
}
