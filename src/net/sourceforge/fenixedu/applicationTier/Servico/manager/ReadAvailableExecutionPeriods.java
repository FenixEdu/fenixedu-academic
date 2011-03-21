package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAvailableExecutionPeriods extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List run(List<Integer> unavailableExecutionPeriodsIDs) throws FenixServiceException {

	final Collection<ExecutionSemester> filteredExecutionPeriods = new ArrayList<ExecutionSemester>(rootDomainObject
		.getExecutionPeriodsSet());
	for (final Integer executionPeriodID : unavailableExecutionPeriodsIDs) {
	    final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	    filteredExecutionPeriods.remove(executionSemester);
	}
	return (List) CollectionUtils.collect(filteredExecutionPeriods, TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD);
    }

    private static final Transformer TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD = new Transformer() {
	@Override
	public Object transform(Object executionPeriod) {
	    return InfoExecutionPeriod.newInfoFromDomain((ExecutionSemester) executionPeriod);
	}
    };

}