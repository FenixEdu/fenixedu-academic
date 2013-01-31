package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;

public class DeleteTSDProcess extends FenixService {
	public void run(Integer tsdProcessId) {
		TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);

		tsdProcess.delete();
	}
}
