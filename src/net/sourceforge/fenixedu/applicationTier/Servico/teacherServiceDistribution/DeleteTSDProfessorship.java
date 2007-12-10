package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;

public class DeleteTSDProfessorship extends Service {
	public void run(Integer tsdProfessorshipId) {
		TSDProfessorship tsdProfessorship = rootDomainObject.readTSDProfessorshipByOID(tsdProfessorshipId);
		TSDTeacher tsdTeacher = tsdProfessorship.getTSDTeacher();
		TSDCourse tsdCourse = tsdProfessorship.getTSDCourse();
		
		for(TSDProfessorship professorship : tsdTeacher.getTSDProfessorShipsByCourse(tsdCourse)){
			professorship.delete();
		}
	}
}
