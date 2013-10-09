package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeletePersistentGroup {

    @Atomic
    public static void run(PersistentGroupMembers groupMembers) {
        check(RolePredicates.MANAGER_PREDICATE);
        if (groupMembers != null) {
            groupMembers.delete();
        }
    }
}