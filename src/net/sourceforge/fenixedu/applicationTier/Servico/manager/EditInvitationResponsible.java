package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class EditInvitationResponsible extends Service {

    public void run(Invitation invitation, Party responsible) {
	if (invitation != null && responsible != null) {
	    invitation.setResponsible(responsible);
	}	
    }
}
