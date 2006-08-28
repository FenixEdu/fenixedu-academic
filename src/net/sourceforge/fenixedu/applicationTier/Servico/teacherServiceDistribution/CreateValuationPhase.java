package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class CreateValuationPhase extends Service {
	public ValuationPhase run(Integer teacherServiceDistributionId, String name) {
		TeacherServiceDistribution teacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId);
		
		return teacherServiceDistribution.createValuationPhase(name);
	}
}
