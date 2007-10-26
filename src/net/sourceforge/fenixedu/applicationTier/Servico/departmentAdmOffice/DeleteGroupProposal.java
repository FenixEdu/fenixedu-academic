package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;

public class DeleteGroupProposal extends Service {

    public void run(final GroupProposal groupProposal) {
	if (groupProposal != null) {
	    groupProposal.delete();
	}
    }
    
}
