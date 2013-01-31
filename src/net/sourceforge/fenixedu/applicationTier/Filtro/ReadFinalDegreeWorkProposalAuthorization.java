/*
 * Created on 2004/04/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposalAuthorization extends Filtro {

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
		IUserView id = getRemoteUser(request);
		Integer finalDegreeWorkProposalOID = (Integer) request.getServiceParameters().parametersArray()[0];
		if (finalDegreeWorkProposalOID != null) {
			Proposal proposal = rootDomainObject.readProposalByOID(finalDegreeWorkProposalOID);
			if (proposal != null) {
				if (proposal.canBeReadBy(id)) {
					return;
				}
			}
		}
		throw new NotAuthorizedFilterException();
	}

}