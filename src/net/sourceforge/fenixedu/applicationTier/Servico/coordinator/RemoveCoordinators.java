package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveCoordinators extends Service {

    public void run(Integer executionDegreeID, List<Integer> coordinatorsToRemoveIDs) throws ExcepcaoPersistencia {

        for (final Integer coordinatorToRemoveID : coordinatorsToRemoveIDs) {
            final Coordinator coordinator = rootDomainObject.readCoordinatorByOID(coordinatorToRemoveID);
            if (coordinator != null) {
                final Teacher teacher = coordinator.getTeacher();
                coordinator.delete();
                if (! teacher.hasAnyCoordinators()) {
                    teacher.getPerson().removeRoleByType(RoleType.COORDINATOR);
                }
            }
        }
    }

}