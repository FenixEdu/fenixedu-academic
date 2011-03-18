package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCompetenceCourses extends FenixService {
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer[] competenceCourseIDs) {
	for (Integer competenceCourseID : competenceCourseIDs) {
	    CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseID);
	    if (competenceCourse != null) {
		competenceCourse.delete();
	    }
	}
    }
}