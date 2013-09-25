package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadAvailableExecutionPeriods {

    @Atomic
    public static List run(List<String> unavailableExecutionPeriodsIDs) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final Collection<ExecutionSemester> filteredExecutionPeriods =
                new ArrayList<ExecutionSemester>(RootDomainObject.getInstance().getExecutionPeriodsSet());
        for (final String executionPeriodID : unavailableExecutionPeriodsIDs) {
            final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);
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