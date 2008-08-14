package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionYearsService extends Service {

    public List run() {
	final List<InfoExecutionYear> infoExecutionYears = new ArrayList<InfoExecutionYear>();
	for (final ExecutionYear executionYear : rootDomainObject.getExecutionYears()) {
	    infoExecutionYears.add(InfoExecutionYear.newInfoFromDomain(executionYear));
	}
	return infoExecutionYears;
    }

    public ExecutionYear run(Integer executionYearID) {
	return rootDomainObject.readExecutionYearByOID(executionYearID);
    }
}