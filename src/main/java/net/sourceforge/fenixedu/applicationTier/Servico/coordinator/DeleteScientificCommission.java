package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteScientificCommission extends FenixService {

    protected void run(Integer executionDegreeId, ScientificCommission commission) {
        ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);

        if (!executionDegree.getScientificCommissionMembers().contains(commission)) {
            throw new DomainException("scientificCommission.delete.incorrectExecutionDegree");
        }

        commission.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteScientificCommission serviceInstance = new DeleteScientificCommission();

    @Service
    public static void runDeleteScientificCommission(Integer executionDegreeId, ScientificCommission commission)
            throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute();
        serviceInstance.run(executionDegreeId, commission);
    }

}