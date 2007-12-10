package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;

public class DissociateTSDTeacherWithTeacherServiceDistribution extends Service {
	public void run(Integer tsdId, Integer tsdTeacherId) {
		TeacherServiceDistribution tsd =  rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
		TSDTeacher tsdTeacher = rootDomainObject.readTSDTeacherByOID(tsdTeacherId);
		
		tsd.removeTSDTeacherFromAllChilds(tsdTeacher);
	}
}
