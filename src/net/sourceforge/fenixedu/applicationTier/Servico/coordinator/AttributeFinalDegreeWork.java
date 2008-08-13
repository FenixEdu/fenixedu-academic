package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class AttributeFinalDegreeWork extends Service {

    public void run(Integer selectedGroupProposal) {
	GroupProposal groupProposal = rootDomainObject.readGroupProposalByOID(selectedGroupProposal);
	if (groupProposal != null) {
	    Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
	    proposal.setGroupAttributed(groupProposal.getFinalDegreeDegreeWorkGroup());

	    FinalDegreeWorkGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
	    for (GroupProposal otherGroupProposal : group.getGroupProposals()) {
		if (!(otherGroupProposal == groupProposal)
			&& otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() != null
			&& (otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() == group)) {
		    Proposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
		    otherProposal.removeGroupAttributed();
		}
	    }
	}
    }

}