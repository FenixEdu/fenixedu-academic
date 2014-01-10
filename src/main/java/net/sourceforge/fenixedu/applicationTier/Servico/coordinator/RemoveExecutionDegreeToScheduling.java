package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixframework.Atomic;

public class RemoveExecutionDegreeToScheduling {

    private class SchedulingContainsProposalsException extends FenixServiceException {
    }

    protected void run(final Scheduleing scheduleing, final ExecutionDegree executionDegree) throws FenixServiceException {
        if (!scheduleing.getProposalsSet().isEmpty()
                || (executionDegree.getScheduling() != null && executionDegree.getScheduling().getProposalsSet().isEmpty())) {
            throw new SchedulingContainsProposalsException();
        }
        scheduleing.getExecutionDegrees().remove(executionDegree);
    }

    // Service Invokers migrated from Berserk

    private static final RemoveExecutionDegreeToScheduling serviceInstance = new RemoveExecutionDegreeToScheduling();

    @Atomic
    public static void runRemoveExecutionDegreeToScheduling(Scheduleing scheduleing, ExecutionDegree executionDegree)
            throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
            serviceInstance.run(scheduleing, executionDegree);
        } catch (NotAuthorizedException ex1) {
            try {
                CoordinatorAuthorizationFilter.instance.execute();
                serviceInstance.run(scheduleing, executionDegree);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}