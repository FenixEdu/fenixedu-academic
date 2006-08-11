package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllExecutionPeriods extends Service {

	public List run() throws ExcepcaoPersistencia {
		final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
		for (final ExecutionPeriod executionPeriod : rootDomainObject.getExecutionPeriods()) {
			infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
		}
		return infoExecutionPeriods;
	}

}