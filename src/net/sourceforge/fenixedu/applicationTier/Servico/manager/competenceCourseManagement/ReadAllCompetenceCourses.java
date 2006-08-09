package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;

public class ReadAllCompetenceCourses extends Service{

	public List<InfoCompetenceCourse> run() throws Exception {

        final List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
        for (final CompetenceCourse competenceCourse : CompetenceCourse.readOldCompetenceCourses()) {
			result.add(InfoCompetenceCourse.newInfoFromDomain(competenceCourse));
		}
		return result;
	}
}
