/*
 * Created on Jan 5, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import pt.ist.fenixWebFramework.services.Service;

public class DeletePersonFunction extends FenixService {

    protected void run(Integer personFunctionID) throws FenixServiceException {
        PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(personFunctionID);
        if (personFunction == null) {
            throw new FenixServiceException("error.delete.personFunction.no.personFunction");
        }
        personFunction.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeletePersonFunction serviceInstance = new DeletePersonFunction();

    @Service
    public static void runDeletePersonFunction(Integer personFunctionID) throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(personFunctionID);
        } catch (NotAuthorizedException ex1) {
            try {
                OperatorAuthorizationFilter.instance.execute();
                serviceInstance.run(personFunctionID);
            } catch (NotAuthorizedException ex2) {
                try {
                    ScientificCouncilAuthorizationFilter.instance.execute();
                    serviceInstance.run(personFunctionID);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}