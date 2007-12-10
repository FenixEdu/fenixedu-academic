package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType;

public class SetTSDProfessorship extends Service {
	public TSDProfessorship run(
		Integer tsdCourseId, 
		Integer tsdTeacherId, 
		Map<String, Object> tsdCourseParameters) {
		
		TSDCourse tsdCourse = rootDomainObject.readTSDCourseByOID(tsdCourseId);
		TSDTeacher tsdTeacher = rootDomainObject.readTSDTeacherByOID(tsdTeacherId);		
		ShiftType type = ShiftType.valueOf((String)tsdCourseParameters.get("shiftType"));
		
		TSDProfessorship tsdProfessorship = tsdCourse.getTSDProfessorshipByTSDTeacherAndShiftType(tsdTeacher, type);
		
		if(tsdProfessorship == null) {
			tsdProfessorship = new TSDProfessorship(tsdCourse, tsdTeacher, type);
		}
		
		tsdProfessorship.setHoursManual((Double) tsdCourseParameters.get("hoursManual"));
		tsdProfessorship.setHoursType(TSDValueType.valueOf((String) tsdCourseParameters.get("hoursType")));
		
		return tsdProfessorship;
	}
}
