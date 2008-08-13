package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class AddCourseToTeacherServiceDistribution extends Service {

    public void run(Integer tsdId, final Integer courseId) throws FenixServiceException {

	TeacherServiceDistribution rootTSD = rootDomainObject.readTeacherServiceDistributionByOID(tsdId).getRootTSD();
	CompetenceCourse course = rootDomainObject.readCompetenceCourseByOID(courseId);

	if (!rootTSD.getCompetenceCourses().contains(course)) {
	    for (ExecutionSemester period : rootTSD.getTSDProcessPhase().getTSDProcess().getExecutionPeriods()) {
		if (course.getCurricularCoursesWithActiveScopesInExecutionPeriod(period).size() > 0) {
		    new TSDCompetenceCourse(rootTSD, course, period);
		}
	    }
	}
    }
}
