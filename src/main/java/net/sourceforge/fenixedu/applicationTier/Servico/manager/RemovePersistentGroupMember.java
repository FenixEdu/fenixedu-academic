package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class RemovePersistentGroupMember {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(Person person, PersistentGroupMembers persistentGroupMembers) {
        if (person != null && persistentGroupMembers != null) {
            persistentGroupMembers.removePersons(person);
        }
    }
}