package net.sourceforge.fenixedu.applicationTier.Servico.person.function;

import java.util.Date;
import java.util.List;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonFunctionsByPersonIDAndExecutionYearID extends Service {

    public List<PersonFunction> run(Integer personID, Integer executionYearID)
            throws FenixServiceException, ExcepcaoPersistencia {
        Person person = (Person) persistentObject.readByOID(Person.class, personID);

        List<PersonFunction> personFunctions = null;

        if (executionYearID != null) {
            ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
                    ExecutionYear.class, executionYearID);
            Date beginDate = executionYear.getBeginDate();
            Date endDate = executionYear.getEndDate();
            personFunctions = person.getPersonFuntions(YearMonthDay.fromDateFields(beginDate),
                    YearMonthDay.fromDateFields(endDate));

        } else {
            personFunctions = person.getPersonFunctions();
        }

        return personFunctions;
    }
}