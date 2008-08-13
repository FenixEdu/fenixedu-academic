package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

public class PublishAprovedFinalDegreeWorkProposals extends Service {

    public void run(Integer executionDegreeOID) throws FenixServiceException{
        if (executionDegreeOID != null) {
        	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);
            Set<Proposal> aprovedFinalDegreeWorkProposals = executionDegree.getScheduling().findApprovedProposals();

            if (aprovedFinalDegreeWorkProposals != null && !aprovedFinalDegreeWorkProposals.isEmpty()) {
                for (Proposal proposal : aprovedFinalDegreeWorkProposals) {
                    proposal.setStatus(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
                }
            }
        }
    }

}
