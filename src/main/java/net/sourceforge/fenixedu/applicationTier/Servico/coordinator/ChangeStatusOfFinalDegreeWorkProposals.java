package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Set;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import pt.ist.fenixframework.Atomic;

public class ChangeStatusOfFinalDegreeWorkProposals {

    @Atomic
    public static void run(Set<Proposal> proposals, FinalDegreeWorkProposalStatus status) {
        for (Proposal proposal : proposals) {
            proposal.setStatus(status);
        }
    }

}
