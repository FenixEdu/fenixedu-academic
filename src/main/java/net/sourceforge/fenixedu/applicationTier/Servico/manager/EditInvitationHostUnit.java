package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class EditInvitationHostUnit {

    @Atomic
    public static void run(Invitation invitation, Unit hostUnit) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        if (invitation != null && hostUnit != null) {
            invitation.setParentParty(hostUnit);
        }
    }
}