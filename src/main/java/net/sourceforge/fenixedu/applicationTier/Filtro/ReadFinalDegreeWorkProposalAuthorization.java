/*
 * Created on 2004/04/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposalAuthorization extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Integer finalDegreeWorkProposalOID = (Integer) request.getServiceParameters().parametersArray()[0];
        if (finalDegreeWorkProposalOID != null) {
            Proposal proposal = RootDomainObject.getInstance().readProposalByOID(finalDegreeWorkProposalOID);
            if (proposal != null) {
                if (proposal.canBeReadBy(id)) {
                    return;
                }
            }
        }
        throw new NotAuthorizedFilterException();
    }

}