package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

public class ChangeStatusOfFinalDegreeWorkProposals extends Service {

    public void run(Integer executionDegreeOID, List<Integer> selectedProposalOIDs, FinalDegreeWorkProposalStatus status)
	    throws FenixServiceException {
	if (executionDegreeOID != null && selectedProposalOIDs != null) {
	    for (Integer selectedProposalOID : selectedProposalOIDs) {
		Proposal proposal = rootDomainObject.readProposalByOID(selectedProposalOID);
		proposal.setStatus(status);
	    }
	}
    }

}
