package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadAvailableExecutionPeriods extends Service {

    public List run(List<Integer> unavailableExecutionPeriodsIDs) throws FenixServiceException, ExcepcaoPersistencia {

	final Collection<ExecutionSemester> filteredExecutionPeriods = new ArrayList<ExecutionSemester>(rootDomainObject
		.getExecutionPeriodsSet());
	for (final Integer executionPeriodID : unavailableExecutionPeriodsIDs) {
	    final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	    filteredExecutionPeriods.remove(executionSemester);
	}
	return (List) CollectionUtils.collect(filteredExecutionPeriods, TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD);
    }

    private Transformer TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD = new Transformer() {
	public Object transform(Object executionPeriod) {
	    return InfoExecutionPeriod.newInfoFromDomain((ExecutionSemester) executionPeriod);
	}
    };

}