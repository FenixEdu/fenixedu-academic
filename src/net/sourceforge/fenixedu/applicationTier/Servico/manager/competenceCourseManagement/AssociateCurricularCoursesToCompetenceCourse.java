package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;

public class AssociateCurricularCoursesToCompetenceCourse extends Service {
	public void run(Integer competenceCourseID, Integer[] curricularCoursesIDs) throws Exception {
		IPersistentCompetenceCourse persistentCompetenceCourse = persistentSupport.getIPersistentCompetenceCourse();
		CompetenceCourse competenceCourse = (CompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceCourseID);
		if(competenceCourse == null) {
			throw new NotExistingServiceException("error.manager.noCompetenceCourse");
		}
		
		IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
		for (Integer curricularCourseID : curricularCoursesIDs) {
			CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, curricularCourseID);
			if(curricularCourse != null) {
				curricularCourses.add(curricularCourse);
			}
		}
		competenceCourse.addCurricularCourses(curricularCourses);
	}
}
