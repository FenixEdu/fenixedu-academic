package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CompetenceCourse;

public class DeleteCompetenceCourses extends Service {
	public void run(Integer[] competenceCourseIDs) throws Exception {
		for (Integer competenceCourseID : competenceCourseIDs) {
			CompetenceCourse competenceCourse = (CompetenceCourse) persistentObject.readByOID(CompetenceCourse.class, competenceCourseID);
			if(competenceCourse != null) {
				competenceCourse.delete();
			}
		}
	}
}
