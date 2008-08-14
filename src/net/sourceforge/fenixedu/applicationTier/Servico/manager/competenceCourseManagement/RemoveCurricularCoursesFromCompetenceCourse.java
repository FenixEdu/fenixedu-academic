package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;

public class RemoveCurricularCoursesFromCompetenceCourse extends Service {
    public void run(Integer competenceCourseID, Integer[] curricularCoursesIDs) throws Exception {
	CompetenceCourse competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseID);
	if (competenceCourse == null) {
	    throw new NotExistingServiceException("error.manager.noCompetenceCourse");
	}

	for (Integer curricularCourseID : curricularCoursesIDs) {
	    CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);
	    if (curricularCourse != null) {
		competenceCourse.getAssociatedCurricularCourses().remove(curricularCourse);
	    }
	}
    }
}
