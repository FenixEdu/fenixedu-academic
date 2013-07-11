package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class GenerateNewPasswordService {

    @Atomic
    public static String run(final String personId) throws FenixServiceException {
        final Person person = (Person) FenixFramework.getDomainObject(personId);
        if (person == null) {
            throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
        }
        final String password = GeneratePassword.getInstance().generatePassword(person);
        person.setPassword(PasswordEncryptor.encryptPassword(password));
        return password;
    }

    @Atomic
    public static String run(Person person) throws FenixServiceException {
        if (person == null) {
            throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
        }
        final String password = GeneratePassword.getInstance().generatePassword(person);
        person.setPassword(PasswordEncryptor.encryptPassword(password));
        return password;
    }
}