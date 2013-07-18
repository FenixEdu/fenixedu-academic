package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteFinalDegreeWorkProposal {

    @Service
    public static void run(final Proposal proposal) {
        if (proposal != null) {
            proposal.delete();
        }
    }

}