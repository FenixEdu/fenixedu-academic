package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import pt.ist.fenixWebFramework.services.Service;

public class PublishAprovedFinalDegreeWorkProposals {
	@Service
	public static void run(ExecutionDegree executionDegree) throws FenixServiceException {
		if (executionDegree != null) {
			Set<Proposal> aprovedFinalDegreeWorkProposals = executionDegree.getScheduling().findApprovedProposals();

			if (aprovedFinalDegreeWorkProposals != null && !aprovedFinalDegreeWorkProposals.isEmpty()) {
				for (Proposal proposal : aprovedFinalDegreeWorkProposals) {
					proposal.setStatus(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
				}
			}
		}
	}

}
