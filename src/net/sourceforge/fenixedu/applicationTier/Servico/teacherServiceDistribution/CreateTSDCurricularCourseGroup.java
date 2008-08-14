package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CreateTSDCurricularCourseGroup extends Service {
    public TSDCurricularCourseGroup run(Integer tsdId, Integer[] tsdCurricularCourseToGroupArray) {

	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
	List<TSDCurricularCourse> tsdCurricularCourseList = new ArrayList<TSDCurricularCourse>();
	TSDCurricularCourseGroup tsdCurricularCourseGroup = null;

	for (Integer tsdCurricularCourseId : tsdCurricularCourseToGroupArray) {
	    TSDCurricularCourse tsdCurricularCourse = (TSDCurricularCourse) rootDomainObject
		    .readTSDCourseByOID(tsdCurricularCourseId);

	    if (tsdCurricularCourse != null) {
		tsdCurricularCourseList.add(tsdCurricularCourse);
	    }
	}

	if (!tsdCurricularCourseList.isEmpty()) {
	    tsdCurricularCourseGroup = new TSDCurricularCourseGroup(tsd, tsdCurricularCourseList);
	    tsdCurricularCourseGroup.setIsActive(true);
	}

	return tsdCurricularCourseGroup;
    }
}
