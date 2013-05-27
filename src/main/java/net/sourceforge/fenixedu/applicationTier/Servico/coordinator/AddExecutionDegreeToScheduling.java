package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;


import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixWebFramework.services.Service;

public class AddExecutionDegreeToScheduling {

    public class SchedulingContainsProposalsException extends FenixServiceException {
    }

    protected void run(final Scheduleing scheduleing, final ExecutionDegree executionDegree)
            throws SchedulingContainsProposalsException {
        if (!scheduleing.getProposalsSet().isEmpty()
                || (executionDegree.getScheduling() != null && executionDegree.getScheduling().getProposalsSet().isEmpty())) {
            throw new SchedulingContainsProposalsException();
        }
        if (!scheduleing.getExecutionDegrees().contains(executionDegree)) {
            scheduleing.getExecutionDegrees().add(executionDegree);
        }
    }

    // Service Invokers migrated from Berserk

    private static final AddExecutionDegreeToScheduling serviceInstance = new AddExecutionDegreeToScheduling();

    @Service
    public static void runAddExecutionDegreeToScheduling(Scheduleing scheduleing, ExecutionDegree executionDegree)
            throws SchedulingContainsProposalsException, NotAuthorizedException {
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