package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;


import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ChangePersonPassword {

    @Service
    public static void run(Integer personID, String password) throws FenixServiceException {
        final Person person = (Person) RootDomainObject.getInstance().readPartyByOID(personID);
        if (person == null) {
            throw new ExcepcaoInexistente("error.changePersonPassword");
        }
        person.setPassword(PasswordEncryptor.encryptPassword(password));
    }
}