package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class AddExamCoordinatorsToVigilantGroup extends FenixService {

    public void run(List<ExamCoordinator> coordinators, VigilantGroup group) {
	for (ExamCoordinator coordinator : coordinators) {
	    group.addExamCoordinators(coordinator);
	}
    }
}
