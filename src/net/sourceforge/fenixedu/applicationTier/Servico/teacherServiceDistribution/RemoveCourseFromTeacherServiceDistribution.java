package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveCourseFromTeacherServiceDistribution extends Service {

	public void run(Integer tsdId, Integer courseId) throws FenixServiceException{

		TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
		TSDCourse course = rootDomainObject.readTSDCourseByOID(courseId);
		CompetenceCourse competenceCourse = course.getCompetenceCourse();
		
		if(course instanceof TSDVirtualCourseGroup){
			course.delete();
		} else {
			for(TSDCourse tsdCourse : tsd.getTSDCoursesByCompetenceCourse(competenceCourse)) {
				tsdCourse.delete();
			}
		}
	}
}
