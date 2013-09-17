package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class SetPersonRoles {

    @Atomic
    public static void run(final Person person, final Set<Role> roles) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        person.indicatePrivledges(roles);
    }

}