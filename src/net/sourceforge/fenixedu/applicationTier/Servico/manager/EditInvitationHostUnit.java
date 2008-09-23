package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class EditInvitationHostUnit extends FenixService {

    public void run(Invitation invitation, Unit hostUnit) {
	if (invitation != null && hostUnit != null) {
	    invitation.setParentParty(hostUnit);
	}
    }
}
