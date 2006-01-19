package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class SetPersonRoles implements IService {

    public Boolean run(final String username, final List<Integer> roleOIDs) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final Person person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(username);

        final List<Role> roles = new ArrayList<Role>();
        for (final Integer roleId : roleOIDs) {
            roles.add((Role) persistentSuport.getIPersistentObject().readByOID(Role.class, roleId));
        }

        person.indicatePrivledges(roles);

        return Boolean.TRUE;
    }

}
