package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhaseStatus;

public class DeleteTSDProcessPhase extends Service {
    public void run(Integer tsdProcessPhaseId) {
	TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);

	if (tsdProcessPhase.getStatus() == TSDProcessPhaseStatus.CLOSED) {
	    if (tsdProcessPhase.getPreviousTSDProcessPhase() != null) {
		tsdProcessPhase.getPreviousTSDProcessPhase().setNextTSDProcessPhase(tsdProcessPhase.getNextTSDProcessPhase());
	    }

	    if (tsdProcessPhase.getNextTSDProcessPhase() != null) {
		tsdProcessPhase.getNextTSDProcessPhase().setPreviousTSDProcessPhase(tsdProcessPhase.getPreviousTSDProcessPhase());
	    }

	    tsdProcessPhase.deleteDataAndPhase();
	}
    }
}
