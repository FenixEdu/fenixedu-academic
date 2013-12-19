/*
 * Created on 2004/04/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposalAuthorization {

    public static final ReadFinalDegreeWorkProposalAuthorization instance = new ReadFinalDegreeWorkProposalAuthorization();

    public void execute(String DegreeWorkProposalOID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        String finalDegreeWorkProposalOID = DegreeWorkProposalOID;
        if (finalDegreeWorkProposalOID != null) {
            Proposal proposal = FenixFramework.getDomainObject(finalDegreeWorkProposalOID);
            if (proposal != null) {
                if (proposal.canBeReadBy(id)) {
                    return;
                }
            }
        }
        throw new NotAuthorizedException();
    }

}