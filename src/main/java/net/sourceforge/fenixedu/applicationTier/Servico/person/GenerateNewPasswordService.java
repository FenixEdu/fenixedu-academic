package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class GenerateNewPasswordService {

    @Service
    public static String run(final String personId) throws FenixServiceException {
        final Person person = (Person) AbstractDomainObject.fromExternalId(personId);
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