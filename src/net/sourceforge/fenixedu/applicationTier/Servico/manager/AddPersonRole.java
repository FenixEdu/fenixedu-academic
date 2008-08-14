package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class AddPersonRole extends Service {

    public void run(final Person person, final RoleType roleType) {
	if (person != null && roleType != null) {
	    person.addPersonRoles(Role.getRoleByRoleType(roleType));
	}
    }

}
