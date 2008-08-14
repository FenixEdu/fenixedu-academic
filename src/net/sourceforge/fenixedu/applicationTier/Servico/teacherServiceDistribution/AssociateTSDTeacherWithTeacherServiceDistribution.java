package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class AssociateTSDTeacherWithTeacherServiceDistribution extends Service {
    public void run(Integer tsdId, Integer tsdTeacherId) {
	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);

	if (tsdTeacherId == null) {
	    tsd.getTSDTeachersSet().addAll(tsd.getParent().getTSDTeachers());
	} else {
	    tsd.addTSDTeachers(rootDomainObject.readTSDTeacherByOID(tsdTeacherId));
	}
    }
}
