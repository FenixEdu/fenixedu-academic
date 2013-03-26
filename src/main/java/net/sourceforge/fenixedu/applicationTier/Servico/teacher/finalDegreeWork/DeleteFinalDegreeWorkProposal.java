/*
 * Created on 2004/03/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author Luis Cruz
 */
public class DeleteFinalDegreeWorkProposal extends FenixService {

    public void run(Integer finalDegreeWorkProposalOID) throws FenixServiceException {
        rootDomainObject.readProposalByOID(finalDegreeWorkProposalOID).delete();
    }

}
