package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class AssociateValuationTeacherWithValuationGrouping extends Service {
	public void run(Integer valuationGroupingId, Integer valuationTeacherId) {
		ValuationGrouping valuationGrouping =  rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		if(valuationTeacherId == null){
			valuationGrouping.getValuationTeachersSet().addAll(valuationGrouping.getParent().getValuationTeachers());
		} else {
			valuationGrouping.addValuationTeachers(rootDomainObject.readValuationTeacherByOID(valuationTeacherId));	
		}		
	}
}
