package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateNewInvitedPerson {

    @Atomic
    public static Invitation run(InvitedPersonBean bean) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Person person = new Person(bean);
        Invitation invitation = new Invitation(person, bean.getUnit(), bean.getResponsible(), bean.getBegin(), bean.getEnd());
        return invitation;
    }
}