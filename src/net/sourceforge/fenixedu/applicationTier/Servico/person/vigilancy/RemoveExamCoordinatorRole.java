package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveExamCoordinatorRole extends Service {

    public void run(Person person) throws ExcepcaoPersistencia {

        person.removeRoleByType(RoleType.EXAM_COORDINATOR);

    }

}
