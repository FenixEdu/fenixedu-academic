package net.sourceforge.fenixedu.applicationTier.Servico.person.function;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

/**
 * 
 * @author naat
 * 
 */
public class ReadPersonFunctionsByPersonIDAndExecutionYearID extends Service {

    public List<PersonFunction> run(Integer personID, Integer executionYearID)
            throws FenixServiceException, ExcepcaoPersistencia {

        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
        IPersistentExecutionYear persistentExecutionYear = persistentSupport
                .getIPersistentExecutionYear();
        Person person = (Person) persistentPerson.readByOID(Person.class, personID);

        List<PersonFunction> personFunctions = null;

        if (executionYearID != null) {
            ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);
            personFunctions = person.getPersonFuntions(executionYear.getBeginDate(), executionYear
                    .getEndDate());

        } else {
            personFunctions = person.getPersonFunctions();
        }

        return personFunctions;
    }
}