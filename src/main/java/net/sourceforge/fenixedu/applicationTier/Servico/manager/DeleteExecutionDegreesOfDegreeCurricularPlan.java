package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteExecutionDegreesOfDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List run(List<Integer> executionDegreesIds) throws FenixServiceException {
        List<String> undeletedExecutionDegreesYears = new ArrayList<String>();

        for (final Integer executionDegreeId : executionDegreesIds) {
            final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);

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