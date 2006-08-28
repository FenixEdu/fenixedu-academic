package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhaseStatus;

public class DeleteValuationPhase extends Service {
	public void run(Integer valuationPhaseId) {
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		
		if(valuationPhase.getStatus() == ValuationPhaseStatus.CLOSED) {			
			if(valuationPhase.getPreviousValuationPhase() != null) {
				valuationPhase.getPreviousValuationPhase().setNextValuationPhase(valuationPhase.getNextValuationPhase());
			}
					
			if(valuationPhase.getNextValuationPhase() != null) {
				valuationPhase.getNextValuationPhase().setPreviousValuationPhase(valuationPhase.getPreviousValuationPhase());
			}
			
			valuationPhase.deleteDataAndPhase();
		}
	}
}
