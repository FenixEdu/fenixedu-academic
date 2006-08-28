package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class CopyValuationPhaseDataToValuationPhase extends Service {
	
	public void run(Integer oldValuationPhaseId, Integer newValuationPhaseId) {
		ValuationPhase oldValuationPhase = rootDomainObject.readValuationPhaseByOID(oldValuationPhaseId);
		ValuationPhase newValuationPhase = rootDomainObject.readValuationPhaseByOID(newValuationPhaseId);
		
		newValuationPhase.copyDataFromValuationPhase(oldValuationPhase);
	}
}
