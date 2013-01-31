package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteExpectationEvaluationGroup extends FenixService {

	@Checked("RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE")
	@Service
	public static void run(ExpectationEvaluationGroup group) {
		if (group != null) {
			group.delete();
		}
	}

}