package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteExecutionDegreesOfDegreeCurricularPlan {

    @Atomic
    public static List<String> run(List<String> executionDegreesIds) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        List<String> undeletedExecutionDegreesYears = new ArrayList<String>();

        for (final String executionDegreeId : executionDegreesIds) {
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

            if (executionDegree != null) {
                if (executionDegree.canBeDeleted()) {
                    executionDegree.delete();
                } else {
                    undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
                }
            }
        }

        return undeletedExecutionDegreesYears;
    }

}