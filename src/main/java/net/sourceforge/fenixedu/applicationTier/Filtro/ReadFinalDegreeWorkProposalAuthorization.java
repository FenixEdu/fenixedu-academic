/*
 * Created on 2004/04/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposalAuthorization {

    public static final ReadFinalDegreeWorkProposalAuthorization instance = new ReadFinalDegreeWorkProposalAuthorization();

    public void execute(Integer DegreeWorkProposalOID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        Integer finalDegreeWorkProposalOID = DegreeWorkProposalOID;
        if (finalDegreeWorkProposalOID != null) {
            Proposal proposal = RootDomainObject.getInstance().readProposalByOID(finalDegreeWorkProposalOID);
            if (proposal != null) {
                if (proposal.canBeReadBy(id)) {
                    return;
                }
            }
        }
        throw new NotAuthorizedException();
    }

}