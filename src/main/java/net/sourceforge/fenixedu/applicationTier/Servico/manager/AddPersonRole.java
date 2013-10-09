package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class AddPersonRole {

    @Atomic
    public static void run(final Person person, final RoleType roleType) {
        check(RolePredicates.MANAGER_PREDICATE);
        if (person != null && roleType != null) {
            person.addPersonRoles(Role.getRoleByRoleType(roleType));
        }
    }

}