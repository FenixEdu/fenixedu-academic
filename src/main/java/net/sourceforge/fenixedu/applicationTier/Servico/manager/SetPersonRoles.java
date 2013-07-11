package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class SetPersonRoles {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(final Person person, final Set<Role> roles) {
        person.indicatePrivledges(roles);
    }

}