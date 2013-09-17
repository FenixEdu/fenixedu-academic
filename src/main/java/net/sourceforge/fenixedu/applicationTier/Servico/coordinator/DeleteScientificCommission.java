package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteScientificCommission {

    protected void run(String executionDegreeId, ScientificCommission commission) {
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        if (!executionDegree.getScientificCommissionMembers().contains(commission)) {
            throw new DomainException("scientificCommission.delete.incorrectExecutionDegree");
        }

        commission.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteScientificCommission serviceInstance = new DeleteScientificCommission();

    @Atomic
    public static void runDeleteScientificCommission(String executionDegreeId, ScientificCommission commission)
            throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        serviceInstance.run(executionDegreeId, commission);
    }

}