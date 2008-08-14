package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CopyTSDProcessPhaseService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;

public class CopyTSDProcessPhaseDataToTSDProcessPhase extends Service {

    public void run(Integer oldTSDProcessPhaseId, Integer newTSDProcessPhaseId) {
	TSDProcessPhase oldTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(oldTSDProcessPhaseId);
	TSDProcessPhase newTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(newTSDProcessPhaseId);

	CopyTSDProcessPhaseService service = CopyTSDProcessPhaseService.getInstance();

	service.copyDataFromTSDProcessPhase(newTSDProcessPhase, oldTSDProcessPhase);
    }
}
