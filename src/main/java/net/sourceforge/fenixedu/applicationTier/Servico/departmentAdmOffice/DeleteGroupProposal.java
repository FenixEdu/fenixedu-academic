package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteGroupProposal {

    @Service
    public static void run(final GroupProposal groupProposal) {
        if (groupProposal != null) {
            groupProposal.delete();
        }
    }

}