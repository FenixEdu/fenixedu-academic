package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllCompetenceCourses extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List<InfoCompetenceCourse> run() {

	final List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
	for (final CompetenceCourse competenceCourse : CompetenceCourse.readOldCompetenceCourses()) {
	    result.add(InfoCompetenceCourse.newInfoFromDomain(competenceCourse));
	}
	return result;
    }
}