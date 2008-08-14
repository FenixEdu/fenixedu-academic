package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class DeleteFinalDegreeWorkProposal extends Service {

    public void run(final Proposal proposal) {
	if (proposal != null) {
	    proposal.delete();
	}
    }

}
