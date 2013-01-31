package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class ChangePersonPassword extends FenixService {

	@Service
	public static void run(Integer personID, String password) throws FenixServiceException {
		final Person person = (Person) rootDomainObject.readPartyByOID(personID);
		if (person == null) {
			throw new ExcepcaoInexistente("error.changePersonPassword");
		}
		person.setPassword(PasswordEncryptor.encryptPassword(password));
	}
}