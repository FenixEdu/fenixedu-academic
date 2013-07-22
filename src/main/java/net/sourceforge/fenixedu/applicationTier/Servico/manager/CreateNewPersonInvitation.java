package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateNewPersonInvitation {

    @Atomic
    public static void run(InvitedPersonBean bean) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        new Invitation(bean.getInvitedPerson(), bean.getUnit(), bean.getResponsible(), bean.getBegin(), bean.getEnd());
    }
}