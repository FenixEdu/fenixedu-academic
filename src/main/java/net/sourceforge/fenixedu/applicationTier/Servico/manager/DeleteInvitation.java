package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteInvitation {

    @Atomic
    public static void run(Invitation invitation) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        if (invitation != null) {
            invitation.delete();
        }
    }
}