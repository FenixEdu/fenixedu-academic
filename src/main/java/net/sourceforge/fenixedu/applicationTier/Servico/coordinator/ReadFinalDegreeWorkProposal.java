/*
 * Created on 2004/03/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposal extends FenixService {

    public InfoProposal run(Integer finalDegreeWorkProposalOID) throws FenixServiceException {
        return InfoProposal.newInfoFromDomain(rootDomainObject.readProposalByOID(finalDegreeWorkProposalOID));
    }

}
