package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCompetenceCourseInformationChangeRequest extends FenixService {

	@Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
	@Service
	public static void run(CompetenceCourseInformationChangeRequest request) {
		request.delete();
	}
}