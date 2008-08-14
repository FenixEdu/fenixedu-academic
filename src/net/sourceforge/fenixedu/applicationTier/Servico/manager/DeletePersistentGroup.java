package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;

public class DeletePersistentGroup extends Service {

    public void run(PersistentGroupMembers groupMembers) {
	if (groupMembers != null) {
	    groupMembers.delete();
	}
    }
}
