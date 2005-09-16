package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoveCoordinators implements IService {

    public Boolean run(Integer executionDegreeID, List coordinatorsIds) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();

        for (final Iterator iter = coordinatorsIds.iterator(); iter.hasNext(); ) {
            ICoordinator coordinator = (ICoordinator) persistentCoordinator.readByOID(Coordinator.class, (Integer) iter.next());

            if (coordinator != null) {
                ITeacher teacher = coordinator.getTeacher();

                coordinator.setExecutionDegree(null);
                coordinator.setTeacher(null);

                persistentCoordinator.deleteByOID(Coordinator.class, coordinator.getIdInternal());

                if (teacher.getCoordinators().isEmpty()) {
                    IPerson person = teacher.getPerson();
                    if (teacher.getCoordinators().isEmpty()) {
                        person.removeRoleByType(RoleType.COORDINATOR);
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}