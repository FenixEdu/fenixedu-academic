package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ChangePersonUsername extends Service {

    public void run(String newUsername, Integer personId, RoleType roleType) {
        Person person = (Person) rootDomainObject.readPartyByOID(personId);              
        person.changeUsername(roleType);
    }
}