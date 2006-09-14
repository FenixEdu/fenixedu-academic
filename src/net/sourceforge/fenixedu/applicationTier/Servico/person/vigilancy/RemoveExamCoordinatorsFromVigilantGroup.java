package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveExamCoordinatorsFromVigilantGroup extends Service {
    public void run(List<ExamCoordinator> coordinators, VigilantGroup group) throws ExcepcaoPersistencia {
        for (ExamCoordinator coordinator : coordinators) {
            group.removeExamCoordinators(coordinator);
        }
    }
}
