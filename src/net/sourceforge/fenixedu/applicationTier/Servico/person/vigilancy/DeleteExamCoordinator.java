package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteExamCoordinator extends Service {

    public void run(ExamCoordinator coordinator) throws ExcepcaoPersistencia {

        Person person = coordinator.getPerson();
        person.removeRoleByType(RoleType.EXAM_COORDINATOR);
        coordinator.delete();

    }

}
