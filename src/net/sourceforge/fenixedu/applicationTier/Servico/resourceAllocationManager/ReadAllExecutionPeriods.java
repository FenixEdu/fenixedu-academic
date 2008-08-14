package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllExecutionPeriods extends Service {

    public List run() {
	final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
	for (final ExecutionSemester executionSemester : rootDomainObject.getExecutionPeriods()) {
	    infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
	}
	return infoExecutionPeriods;
    }

}