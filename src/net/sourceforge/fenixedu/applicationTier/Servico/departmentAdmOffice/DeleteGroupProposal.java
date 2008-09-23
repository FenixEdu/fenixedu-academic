package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;

public class DeleteGroupProposal extends FenixService {

    public void run(final GroupProposal groupProposal) {
	if (groupProposal != null) {
	    groupProposal.delete();
	}
    }

}
