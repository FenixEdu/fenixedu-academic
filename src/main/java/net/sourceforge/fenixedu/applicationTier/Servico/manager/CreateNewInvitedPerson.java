package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateNewInvitedPerson {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static Invitation run(InvitedPersonBean bean) {
        Person person = new Person(bean);
        Invitation invitation = new Invitation(person, bean.getUnit(), bean.getResponsible(), bean.getBegin(), bean.getEnd());
        person.setIstUsername();
        return invitation;
    }
}