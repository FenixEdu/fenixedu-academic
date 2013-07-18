package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemovePersistentGroupMember {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Person person, PersistentGroupMembers persistentGroupMembers) {
        if (person != null && persistentGroupMembers != null) {
            persistentGroupMembers.removePersons(person);
        }
    }
}