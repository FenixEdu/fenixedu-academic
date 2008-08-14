package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCurrentExecutionYear extends Service {

    public InfoExecutionYear run() {
	return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
    }

}