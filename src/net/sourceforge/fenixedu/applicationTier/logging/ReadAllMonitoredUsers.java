package net.sourceforge.fenixedu.applicationTier.logging;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.log.IMonitoredUser;
import net.sourceforge.fenixedu.domain.log.MonitoredUser;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class ReadAllMonitoredUsers implements IService {

    public Collection<IMonitoredUser> run() throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        return ps.getIPersistentObject().readAll(MonitoredUser.class);

    }

}
