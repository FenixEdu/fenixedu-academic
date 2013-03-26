package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;

public class SetPublishedStateOnTSDProcessPhase extends FenixService {
    public void run(Integer tsdProcessPhaseId, Boolean publishedState) {
        TSDProcessPhase tsdProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(tsdProcessPhaseId);
        tsdProcessPhase.setIsPublished(publishedState);
    }
}
