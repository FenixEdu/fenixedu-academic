package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ManagementGroups;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

public class ProtocolsManagementGroup extends Filtro {

    final public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {

	if (!ManagementGroups.isProtocolManagerMember(getRemoteUser(request).getPerson())) {
	    throw new NotAuthorizedFilterException();
	}
    }
}