package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixframework.Atomic;

public class DeleteFinalDegreeWorkProposal {

    @Atomic
    public static void run(final Proposal proposal) {
        if (proposal != null) {
            proposal.delete();
        }
    }

}