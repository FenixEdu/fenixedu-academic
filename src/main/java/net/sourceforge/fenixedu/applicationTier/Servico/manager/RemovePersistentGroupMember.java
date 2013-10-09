package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class RemovePersistentGroupMember {

    @Atomic
    public static void run(Person person, PersistentGroupMembers persistentGroupMembers) {
        check(RolePredicates.MANAGER_PREDICATE);
        if (person != null && persistentGroupMembers != null) {
            persistentGroupMembers.removePersons(person);
        }
    }
}