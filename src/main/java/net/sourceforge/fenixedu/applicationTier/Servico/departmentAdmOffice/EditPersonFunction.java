/*
 * Created on Oct 28, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class EditPersonFunction {

    protected void run(String personFunctionID, String functionID, YearMonthDay beginDate, YearMonthDay endDate, Double credits)
            throws FenixServiceException, DomainException {

        PersonFunction person_Function = (PersonFunction) FenixFramework.getDomainObject(personFunctionID);

        if (person_Function == null) {
            throw new FenixServiceException("error.no.personFunction");
        }

        Function function = (Function) FenixFramework.getDomainObject(functionID);

        if (function == null) {
            throw new FenixServiceException("erro.noFunction");
        }

        person_Function.edit(beginDate, endDate, credits);
    }

    // Service Invokers migrated from Berserk

    private static final EditPersonFunction serviceInstance = new EditPersonFunction();

    @Service
    public static void runEditPersonFunction(String personFunctionID, String functionID, YearMonthDay beginDate,
            YearMonthDay endDate, Double credits) throws FenixServiceException, DomainException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(personFunctionID, functionID, beginDate, endDate, credits);
        } catch (NotAuthorizedException ex1) {
            try {
                OperatorAuthorizationFilter.instance.execute();
                serviceInstance.run(personFunctionID, functionID, beginDate, endDate, credits);
            } catch (NotAuthorizedException ex2) {
                try {
                    ScientificCouncilAuthorizationFilter.instance.execute();
                    serviceInstance.run(personFunctionID, functionID, beginDate, endDate, credits);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}