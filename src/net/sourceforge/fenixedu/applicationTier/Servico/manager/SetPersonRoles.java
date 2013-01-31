package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class SetPersonRoles extends FenixService {

	@Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
	@Service
	public static void run(final Person person, final Set<Role> roles) {
		person.indicatePrivledges(roles);
	}

}