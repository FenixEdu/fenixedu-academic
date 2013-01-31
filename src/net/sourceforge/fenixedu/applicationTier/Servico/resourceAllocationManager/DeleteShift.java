/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteShift extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static void run(InfoShift infoShift) throws FenixServiceException {
		rootDomainObject.readShiftByOID(infoShift.getIdInternal()).delete();
	}

}