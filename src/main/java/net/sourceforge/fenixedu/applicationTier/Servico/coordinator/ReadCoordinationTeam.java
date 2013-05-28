/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationTeam {

    protected List run(Integer executionDegreeId) throws FenixServiceException {
        ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("errors.invalid.execution.degree");
        }
        List<Coordinator> coordinators = executionDegree.getCoordinatorsList();
        Iterator iterator = coordinators.iterator();
        List infoCoordinators = new ArrayList();
        while (iterator.hasNext()) {
            Coordinator coordinator = (Coordinator) iterator.next();
            InfoCoordinator infoCoordinator = InfoCoordinator.newInfoFromDomain(coordinator);
            infoCoordinators.add(infoCoordinator);
        }
        return infoCoordinators;
    }
    // Service Invokers migrated from Berserk

    private static final ReadCoordinationTeam serviceInstance = new ReadCoordinationTeam();

    @Service
    public static List runReadCoordinationTeam(Integer executionDegreeId) throws FenixServiceException  , NotAuthorizedException {
        DegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return serviceInstance.run(executionDegreeId);
    }

}