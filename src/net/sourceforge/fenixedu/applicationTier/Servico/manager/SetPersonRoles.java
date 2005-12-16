package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SetPersonRoles implements IService {

    public Boolean run(final String username, final List<Integer> roleOIDs) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IPerson person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(username);

        final List<IRole> roles = new ArrayList<IRole>();
        for (final Integer roleId : roleOIDs) {
            roles.add((IRole) persistentSuport.getIPersistentObject().readByOID(Role.class, roleId));
        }

        person.indicatePrivledges(roles);

        return Boolean.TRUE;
    }

}
