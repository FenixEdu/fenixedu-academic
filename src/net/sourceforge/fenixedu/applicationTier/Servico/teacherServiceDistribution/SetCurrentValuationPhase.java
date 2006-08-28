package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class SetCurrentValuationPhase extends Service {
	public void run(Integer valuationPhaseId) {
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		
		valuationPhase.setCurrent();
	}
}
