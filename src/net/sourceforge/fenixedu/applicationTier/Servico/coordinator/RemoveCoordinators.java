/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonRole;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota 30/Oct/2003
 * 
 */
public class RemoveCoordinators implements IService {

    public Boolean run(Integer executionDegreeID, List coordinatorsIds) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        IPersistentPersonRole persistentPersonRole = sp.getIPersistentPersonRole();

        for (final Iterator iter = coordinatorsIds.iterator(); iter.hasNext(); ) {
            ICoordinator coordinator = (ICoordinator) persistentCoordinator.readByOID(
                    Coordinator.class, (Integer) iter.next());

            if (coordinator != null) {
                ITeacher teacher = coordinator.getTeacher();

                coordinator.setExecutionDegree(null);
                coordinator.setTeacher(null);

                persistentCoordinator.deleteByOID(Coordinator.class, coordinator.getIdInternal());

                if (teacher.getCoordinators().isEmpty()) {
                    IPerson person = coordinator.getTeacher().getPerson();
                    IPersonRole personRole = persistentPersonRole.readByPersonAndRole(person, sp
                            .getIPersistentRole().readByRoleType(RoleType.COORDINATOR));
                    if (personRole != null) {
                        persistentPersonRole.deleteByOID(PersonRole.class, personRole.getIdInternal());
                    }
                }
            }
        }

        return new Boolean(true);
    }
}