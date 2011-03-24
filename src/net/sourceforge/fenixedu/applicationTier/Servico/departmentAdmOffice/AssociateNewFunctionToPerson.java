/*
 * Created on Oct 27, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

import org.joda.time.YearMonthDay;

public class AssociateNewFunctionToPerson extends FenixService {

    public void run(Integer functionID, Integer personID, Double credits, YearMonthDay begin, YearMonthDay end)
	    throws FenixServiceException, DomainException {

	Person person = (Person) rootDomainObject.readPartyByOID(personID);
	if (person == null) {
	    throw new FenixServiceException("error.noPerson");
	}

	Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
	if (function == null) {
	    throw new FenixServiceException("error.noFunction");
	}

	person.addPersonFunction(function, begin, end, credits);
    }
}
