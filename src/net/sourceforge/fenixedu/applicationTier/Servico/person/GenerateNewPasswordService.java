package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.domain.Person;

public class GenerateNewPasswordService extends Service {

    public String run(final Integer personId) throws FenixServiceException {
	final Person person = (Person) rootDomainObject.readPartyByOID(personId);
	if (person == null) {
	    throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
	}
	final String password = GeneratePassword.getInstance().generatePassword(person);
	person.setPassword(PasswordEncryptor.encryptPassword(password));
	return password;
    }

    public String run(Person person) throws FenixServiceException {
	if (person == null) {
	    throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
	}
	final String password = GeneratePassword.getInstance().generatePassword(person);
	person.setPassword(PasswordEncryptor.encryptPassword(password));
	return password;
    }
}
