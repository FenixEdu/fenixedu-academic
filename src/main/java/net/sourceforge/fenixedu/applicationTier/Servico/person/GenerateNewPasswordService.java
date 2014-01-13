package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class GenerateNewPasswordService {

    @Atomic
    public static String run(Person person) throws FenixServiceException {
        if (person == null || person.getUser() == null) {
            throw new ExcepcaoInexistente("error.generateNewPassword.noPerson");
        }
        return person.getUser().generatePassword();
    }
}
