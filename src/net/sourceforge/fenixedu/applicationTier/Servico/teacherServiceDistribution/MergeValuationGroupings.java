package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class MergeValuationGroupings extends Service {
	public void run(Integer valuationGroupingId, Integer otherGroupingId) {
					
		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		ValuationGrouping otherGrouping = rootDomainObject.readValuationGroupingByOID(otherGroupingId);
	
		valuationGrouping.mergeWithGrouping(otherGrouping);
	}
}
