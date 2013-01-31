package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllCurricularCoursesByCompetenceCourse extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static List<CurricularCourse> run(final Integer competenceID) throws NonExistingServiceException {
		final CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceID);
		if (competenceCourse == null) {
			throw new NonExistingServiceException("error.manager.noCompetenceCourse");
		}

		final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
		for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
			if (!curricularCourse.isBolonhaDegree()) {
				result.add(curricularCourse);
			}
		}

		return result;
	}

}