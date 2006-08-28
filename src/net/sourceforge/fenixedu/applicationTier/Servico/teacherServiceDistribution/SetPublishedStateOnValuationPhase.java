package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class SetPublishedStateOnValuationPhase extends Service {
	public void run(Integer valuationPhaseId, Boolean publishedState) {
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		valuationPhase.setIsPublished(publishedState);
	}
}
