package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;

public class DeletePersistentGroup extends FenixService {

    public void run(PersistentGroupMembers groupMembers) {
	if (groupMembers != null) {
	    groupMembers.delete();
	}
    }
}
