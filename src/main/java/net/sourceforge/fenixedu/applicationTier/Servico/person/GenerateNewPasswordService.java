package net.sourceforge.fenixedu.applicationTier.Servico.person;


import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class GenerateNewPasswordService {

    @Service
    public static String run(final Integer personId) throws FenixServiceException {
        final Person person = (Person) RootDomainObject.getInstance().readPartyByOID(personId);
        if (person == null) {
            throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
        }
        final String password = GeneratePassword.getInstance().generatePassword(person);
        person.setPassword(PasswordEncryptor.encryptPassword(password));
        return password;
    }

    @Service
    public static String run(Person person) throws FenixServiceException {
        if (person == null) {
            throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
        }
        final String password = GeneratePassword.getInstance().generatePassword(person);
        person.setPassword(PasswordEncryptor.encryptPassword(password));
        return password;
    }
}