package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;

public class CreateTSDProcessPhase extends FenixService {
    public TSDProcessPhase run(Integer tsdProcessId, String name) {
        TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);

        return tsdProcess.createTSDProcessPhase(name);
    }
}
