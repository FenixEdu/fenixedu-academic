package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateNewPersonInvitation {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(InvitedPersonBean bean) {
        new Invitation(bean.getInvitedPerson(), bean.getUnit(), bean.getResponsible(), bean.getBegin(), bean.getEnd());
    }
}