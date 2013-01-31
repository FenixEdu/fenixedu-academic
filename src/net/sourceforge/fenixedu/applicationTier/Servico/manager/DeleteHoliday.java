package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Holiday;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteHoliday extends FenixService {

	@Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
	@Service
	public static void run(final Holiday holiday) {
		holiday.delete();
	}

}