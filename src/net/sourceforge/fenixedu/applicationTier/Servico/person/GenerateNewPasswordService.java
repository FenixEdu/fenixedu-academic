package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.domain.Person;

public class GenerateNewPasswordService extends Service {

	public String run(Integer personID) throws Exception {
		Person person = (Person) rootDomainObject.readPartyByOID(personID);
		if (person == null) {
			throw new ExcepcaoInexistente("Unknown Person!");
		}

		String password = GeneratePassword.getInstance().generatePassword(person);

		person.setPassword(PasswordEncryptor.encryptPassword(password));

		return password;
	}

}
