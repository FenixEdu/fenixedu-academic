package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteGroupProposalAttribution {

    @Service
    public static void run(final GroupProposal groupProposal) {
        if (groupProposal != null) {
            groupProposal.deleteAttributions();
        }
    }

}