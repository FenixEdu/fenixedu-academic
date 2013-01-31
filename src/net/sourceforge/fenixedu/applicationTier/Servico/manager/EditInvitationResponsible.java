package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditInvitationResponsible extends FenixService {

	@Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
	@Service
	public static void run(Invitation invitation, Party responsible) {
		if (invitation != null && responsible != null) {
			invitation.setResponsible(responsible);
		}
	}
}