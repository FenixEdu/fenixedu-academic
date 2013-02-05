package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveExamCoordinatorsFromVigilantGroup extends FenixService {
    @Service
    public static void run(List<ExamCoordinator> coordinators, VigilantGroup group) {
        for (ExamCoordinator coordinator : coordinators) {
            group.removeExamCoordinators(coordinator);
        }
    }
}