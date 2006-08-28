package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class DeleteValuationGrouping extends Service {
	public void run(Integer valuationGroupingId) {
		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		valuationGrouping.delete();
	}
}
