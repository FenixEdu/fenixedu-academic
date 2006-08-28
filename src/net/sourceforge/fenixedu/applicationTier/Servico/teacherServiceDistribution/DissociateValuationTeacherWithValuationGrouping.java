package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;

public class DissociateValuationTeacherWithValuationGrouping extends Service {
	public void run(Integer valuationGroupingId, Integer valuationTeacherId) {
		ValuationGrouping valuationGrouping =  rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		ValuationTeacher valuationTeacher = rootDomainObject.readValuationTeacherByOID(valuationTeacherId);
		
		valuationGrouping.removeValuationTeacherFromAllChilds(valuationTeacher);
	}
}
