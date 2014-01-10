package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import pt.ist.fenixframework.Atomic;

public class DeleteGroupProposalAttribution {

    @Atomic
    public static void run(final GroupProposal groupProposal) {
        if (groupProposal != null) {
            groupProposal.deleteAttributions();
        }
    }

}