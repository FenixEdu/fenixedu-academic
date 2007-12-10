package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;

public class CloseTSDProcessPhase extends Service {
	public void run(Integer tsdProcessPhaseId) {
		TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);
		tsdProcessPhase.setClosed();
	}

}
