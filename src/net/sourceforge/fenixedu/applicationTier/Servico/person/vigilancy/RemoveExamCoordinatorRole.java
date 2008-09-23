package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveExamCoordinatorRole extends FenixService {

    public void run(Person person) {

	person.removeRoleByType(RoleType.EXAM_COORDINATOR);

    }

}
