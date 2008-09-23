package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class DeleteFinalDegreeWorkProposal extends FenixService {

    public void run(final Proposal proposal) {
	if (proposal != null) {
	    proposal.delete();
	}
    }

}
