package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.MasterDegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;

public class ReadCurrentExecutionDegreeByDegreeCurricularPlanID extends FenixService {

    protected InfoExecutionDegree run(final Integer degreeCurricularPlanID) {

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);

        final List executionDegrees = degreeCurricularPlan.getExecutionDegrees();
        final ExecutionDegree executionDegree = (ExecutionDegree) CollectionUtils.find(executionDegrees, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                return executionDegree.getExecutionYear().isCurrent();
            }
        });

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

    // Service Invokers migrated from Berserk

    private static final ReadCurrentExecutionDegreeByDegreeCurricularPlanID serviceInstance =
            new ReadCurrentExecutionDegreeByDegreeCurricularPlanID();

    @Service
    public static InfoExecutionDegree runReadCurrentExecutionDegreeByDegreeCurricularPlanID(Integer degreeCurricularPlanID)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(degreeCurricularPlanID);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                return serviceInstance.run(degreeCurricularPlanID);
            } catch (NotAuthorizedException ex2) {
                try {
                    CoordinatorAuthorizationFilter.instance.execute();
                    return serviceInstance.run(degreeCurricularPlanID);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}