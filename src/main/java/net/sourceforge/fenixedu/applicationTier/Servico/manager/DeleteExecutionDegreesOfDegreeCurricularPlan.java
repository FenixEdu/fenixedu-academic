package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteExecutionDegreesOfDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List run(List<String> executionDegreesIds) throws FenixServiceException {
        List<String> undeletedExecutionDegreesYears = new ArrayList<String>();

        for (final String executionDegreeId : executionDegreesIds) {
            final ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeId);

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