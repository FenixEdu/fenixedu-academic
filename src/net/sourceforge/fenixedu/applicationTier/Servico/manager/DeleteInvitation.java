package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;

public class DeleteInvitation extends Service {

    public void run(Invitation invitation) {
	if(invitation != null) {
	    invitation.delete();
	}
    }
}
