package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class AssociateTSDCourseWithTeacherServiceDistribution extends Service {
    public void run(Integer tsdId, Integer tsdcourseId) {
	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);

	if (tsdcourseId == null) {
	    tsd.getTSDCoursesSet().addAll(tsd.getParent().getTSDCourses());
	} else {
	    TSDCourse course = rootDomainObject.readTSDCourseByOID(tsdcourseId);
	    course.addTeacherServiceDistributions(tsd);
	    tsd.getTSDCoursesSet().addAll(tsd.getParent().getTSDCoursesByCompetenceCourse(course.getCompetenceCourse()));
	}
    }
}
