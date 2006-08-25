package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;

public class ChangePersonUsername extends Service {

    public void run(String newUsername, Integer personId) {
        Person person = (Person) rootDomainObject.readPartyByOID(personId);              
        person.changeUsername(newUsername);
    }
}