package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;

public class SetCurrentTSDProcessPhase extends FenixService {
    public void run(Integer tsdProcessPhaseId) {
        TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);

        tsdProcessPhase.setCurrent();
    }
}
