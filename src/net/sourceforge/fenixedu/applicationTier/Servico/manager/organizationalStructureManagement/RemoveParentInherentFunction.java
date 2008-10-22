/*
 * Created on Nov 22, 2005
 *  by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

public class RemoveParentInherentFunction extends FenixService {

    public void run(Integer functionID) throws FenixServiceException, DomainException {

	Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
	if (function == null) {
	    throw new FenixServiceException("error.noFunction");
	}

	function.removeParentInherentFunction();
    }

}
