package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SetPersonRoles extends Service {

    public Boolean run(final String username, final List<Integer> roleOIDs) throws ExcepcaoPersistencia {
        final Person person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername(username);

        final List<Role> roles = new ArrayList<Role>();
        for (final Integer roleId : roleOIDs) {
            roles.add((Role) persistentObject.readByOID(Role.class, roleId));
        }

        person.indicatePrivledges(roles);

        return Boolean.TRUE;
    }

}
