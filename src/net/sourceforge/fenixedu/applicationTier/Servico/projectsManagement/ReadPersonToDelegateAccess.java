/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadPersonToDelegateAccess extends Service {

    public InfoPerson run(String userView, String costCenter, String username, String userNumber) throws FenixServiceException {
	Person person = Person.readPersonByUsername(username);
	if (person == null) {
	    throw new ExcepcaoInexistente();
	} else if (!isTeacherOrEmployee(person)) {
	    throw new InvalidArgumentsServiceException();
	}
	return InfoPerson.newInfoFromDomain(person);
    }

    private boolean isTeacherOrEmployee(Person person) {
	if (person.getTeacher() == null) {
	    if (person.getEmployee() == null) {
		return false;
	    }
	}
	return true;
    }

}