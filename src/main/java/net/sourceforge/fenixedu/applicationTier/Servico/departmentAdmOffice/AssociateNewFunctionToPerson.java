/*
 * Created on Oct 27, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AssociateNewFunctionToPerson {

    protected void run(Integer functionID, Integer personID, Double credits, YearMonthDay begin, YearMonthDay end)
            throws FenixServiceException, DomainException {

        Person person = (Person) AbstractDomainObject.fromExternalId(personID);
        if (person == null) {
            throw new FenixServiceException("error.noPerson");
        }

        Function function = (Function) AbstractDomainObject.fromExternalId(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }

        person.addPersonFunction(function, begin, end, credits);
    }

    // Service Invokers migrated from Berserk

    private static final AssociateNewFunctionToPerson serviceInstance = new AssociateNewFunctionToPerson();

    @Service
    public static void runAssociateNewFunctionToPerson(Integer functionID, Integer personID, Double credits, YearMonthDay begin,
            YearMonthDay end) throws FenixServiceException, DomainException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(functionID, personID, credits, begin, end);
        } catch (NotAuthorizedException ex1) {
            try {
                OperatorAuthorizationFilter.instance.execute();
                serviceInstance.run(functionID, personID, credits, begin, end);
            } catch (NotAuthorizedException ex2) {
                try {
                    ScientificCouncilAuthorizationFilter.instance.execute();
                    serviceInstance.run(functionID, personID, credits, begin, end);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}