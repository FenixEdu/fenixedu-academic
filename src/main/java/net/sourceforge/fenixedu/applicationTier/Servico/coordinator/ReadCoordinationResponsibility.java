/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationResponsibility {

    protected Boolean run(String executionDegreeId, User userView) throws FenixServiceException {
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(userView.getPerson());

        if (coordinator == null || !coordinator.getResponsible().booleanValue()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCoordinationResponsibility serviceInstance = new ReadCoordinationResponsibility();

    @Atomic
    public static Boolean runReadCoordinationResponsibility(String executionDegreeId, User userView)
            throws FenixServiceException, NotAuthorizedException {
        DegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return serviceInstance.run(executionDegreeId, userView);
    }

}