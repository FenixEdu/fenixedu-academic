package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SetPersonRoles extends Service {

    public void run(final Person person, final Set<Role> roles) {
	person.indicatePrivledges(roles);
    }

}
