package net.sourceforge.fenixedu.applicationTier.Servico.person.function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonFunctionsByPersonIDAndExecutionYearID {

    @Checked("RolePredicates.PERSON_PREDICATE")
    @Service
    public static List<PersonFunction> run(Integer personID, Integer executionYearID) throws FenixServiceException {
        Person person = (Person) RootDomainObject.getInstance().readPartyByOID(personID);

        List<PersonFunction> personFunctions = null;

        if (executionYearID != null) {
            ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);
            Date beginDate = executionYear.getBeginDate();
            Date endDate = executionYear.getEndDate();
            personFunctions =
                    person.getPersonFuntions(YearMonthDay.fromDateFields(beginDate), YearMonthDay.fromDateFields(endDate));

        } else {
            personFunctions = new ArrayList<PersonFunction>(person.getPersonFunctions());
        }

        return personFunctions;
    }
}