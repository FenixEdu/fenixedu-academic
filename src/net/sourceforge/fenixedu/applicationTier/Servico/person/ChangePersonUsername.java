package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangePersonUsername extends Service {

    public void run(String newUsername, Integer personId) throws ExcepcaoPersistencia {
        Person person = (Person) rootDomainObject.readPartyByOID(personId);              
        person.changeUsername(newUsername);
    }
}