package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;

public class DeleteCompetenceCourses extends Service {
	public void run(Integer[] competenceCourseIDs) throws Exception {
		IPersistentCompetenceCourse persistentCompetenceCourse = persistentSupport.getIPersistentCompetenceCourse();
		for (Integer competenceCourseID : competenceCourseIDs) {
			CompetenceCourse competenceCourse = (CompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceCourseID);
			if(competenceCourse != null) {
				competenceCourse.delete();
			}
		}
	}
}
