package net.sourceforge.fenixedu.applicationTier.Servico.person;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ChangePersonUsername extends FenixService {

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static void run(String newUsername, Integer personId, RoleType roleType) {
	Person person = (Person) rootDomainObject.readPartyByOID(personId);
	person.changeUsername(roleType);
    }
}