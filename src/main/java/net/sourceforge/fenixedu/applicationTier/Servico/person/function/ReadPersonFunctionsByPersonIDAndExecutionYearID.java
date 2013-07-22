package net.sourceforge.fenixedu.applicationTier.Servico.person.function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonFunctionsByPersonIDAndExecutionYearID {

    @Atomic
    public static List<PersonFunction> run(String personID, String executionYearID) throws FenixServiceException {
        check(RolePredicates.PERSON_PREDICATE);
        Person person = (Person) FenixFramework.getDomainObject(personID);

        List<PersonFunction> personFunctions = null;

        if (executionYearID != null) {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
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