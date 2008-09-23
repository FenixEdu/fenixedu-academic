package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCurrentExecutionYear extends FenixService {

    public InfoExecutionYear run() {
	return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
    }

}