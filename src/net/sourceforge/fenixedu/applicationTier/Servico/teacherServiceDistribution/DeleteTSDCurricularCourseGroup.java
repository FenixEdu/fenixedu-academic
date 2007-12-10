package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;

public class DeleteTSDCurricularCourseGroup extends Service {
	public void run(Integer tsdCurricularCourseGroupId) {
		TSDCurricularCourseGroup tsdCurricularCourseGroup = (TSDCurricularCourseGroup) rootDomainObject.readTSDCourseByOID(tsdCurricularCourseGroupId);
		
		for (TSDCurricularCourse tsdCurricularCourse : tsdCurricularCourseGroup.getTSDCurricularCourses()) {
			tsdCurricularCourse.setTSDCurricularCourseGroup(null);
		}
			
		tsdCurricularCourseGroup.delete();
	}
}
