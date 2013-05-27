/*
 * Created on 2004/03/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Luis Cruz
 */
public class DeleteFinalDegreeWorkProposal {

    public void run(Integer finalDegreeWorkProposalOID) throws FenixServiceException {
        RootDomainObject.getInstance().readProposalByOID(finalDegreeWorkProposalOID).delete();
    }

}
