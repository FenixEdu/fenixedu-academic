package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class DeleteTeacherServiceDistribution extends Service {
    public void run(Integer tsdId) {
	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);

	tsd.delete();
    }
}
