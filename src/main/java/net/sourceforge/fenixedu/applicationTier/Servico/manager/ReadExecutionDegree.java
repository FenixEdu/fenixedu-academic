/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class ReadExecutionDegree {

    protected InfoExecutionDegree run(Integer idInternal) throws FenixServiceException {
        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(idInternal);

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionDegree serviceInstance = new ReadExecutionDegree();

    @Service
    public static InfoExecutionDegree runReadExecutionDegree(Integer idInternal) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(idInternal);
        } catch (NotAuthorizedException ex1) {
            try {
                CoordinatorAuthorizationFilter.instance.execute();
                return serviceInstance.run(idInternal);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}