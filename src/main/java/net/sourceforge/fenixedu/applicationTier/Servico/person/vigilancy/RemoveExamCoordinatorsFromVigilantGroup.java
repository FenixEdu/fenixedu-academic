package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixframework.Atomic;

public class RemoveExamCoordinatorsFromVigilantGroup {
    @Atomic
    public static void run(List<ExamCoordinator> coordinators, VigilantGroup group) {
        for (ExamCoordinator coordinator : coordinators) {
            group.removeExamCoordinators(coordinator);
        }
    }
}