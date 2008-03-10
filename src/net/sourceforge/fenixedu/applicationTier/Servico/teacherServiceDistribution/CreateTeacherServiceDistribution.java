package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CreateTeacherServiceDistribution extends Service {
	public TeacherServiceDistribution run(Integer tsdProcessPhaseId, Integer fatherTeacherServiceDistributionId, String name) {
		TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);
		TeacherServiceDistribution fatherTeacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(fatherTeacherServiceDistributionId);
		
		TeacherServiceDistribution tsd = new TeacherServiceDistribution(tsdProcessPhase, name, fatherTeacherServiceDistribution, 
				new ArrayList<TSDTeacher>(), new ArrayList<TSDCourse>(), null, null,null,null);
				
		return tsd;
	}
}
