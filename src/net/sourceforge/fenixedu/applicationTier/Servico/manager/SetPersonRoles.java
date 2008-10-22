package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;

public class SetPersonRoles extends FenixService {

    public void run(final Person person, final Set<Role> roles) {
	person.indicatePrivledges(roles);
    }

}
