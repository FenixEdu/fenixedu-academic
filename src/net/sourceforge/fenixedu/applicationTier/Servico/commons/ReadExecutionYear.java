package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionYear extends Service {
    public InfoExecutionYear run(String year) {
	return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readExecutionYearByName(year));
    }
}
