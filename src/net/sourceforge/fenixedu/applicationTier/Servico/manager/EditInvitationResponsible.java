package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class EditInvitationResponsible extends FenixService {

    public void run(Invitation invitation, Party responsible) {
	if (invitation != null && responsible != null) {
	    invitation.setResponsible(responsible);
	}
    }
}
