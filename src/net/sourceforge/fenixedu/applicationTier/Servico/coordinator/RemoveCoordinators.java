package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveCoordinators extends Service {

    public Boolean run(Integer executionDegreeID, List coordinatorsIds) throws ExcepcaoPersistencia {
        for (final Iterator iter = coordinatorsIds.iterator(); iter.hasNext(); ) {
            Coordinator coordinator = (Coordinator) persistentObject.readByOID(Coordinator.class, (Integer) iter.next());

            if (coordinator != null) {
                Teacher teacher = coordinator.getTeacher();

                coordinator.setExecutionDegree(null);
                coordinator.setTeacher(null);

                persistentObject.deleteByOID(Coordinator.class, coordinator.getIdInternal());

                if (teacher.getCoordinators().isEmpty()) {
                    Person person = teacher.getPerson();
                    if (teacher.getCoordinators().isEmpty()) {
                        person.removeRoleByType(RoleType.COORDINATOR);
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}