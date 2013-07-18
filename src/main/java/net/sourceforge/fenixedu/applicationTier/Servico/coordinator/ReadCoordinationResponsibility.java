/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;


import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationResponsibility {

    protected Boolean run(Integer executionDegreeId, IUserView userView) throws FenixServiceException {
        ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(userView.getPerson());

        if (coordinator == null || !coordinator.getResponsible().booleanValue()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCoordinationResponsibility serviceInstance = new ReadCoordinationResponsibility();

    @Service
    public static Boolean runReadCoordinationResponsibility(Integer executionDegreeId, IUserView userView)
            throws FenixServiceException, NotAuthorizedException {
        DegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return serviceInstance.run(executionDegreeId, userView);
    }

}