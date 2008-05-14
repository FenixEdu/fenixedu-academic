package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

public class LerAulasDeSalaEmSemestre extends Service {

    public List<InfoLesson> run(InfoExecutionPeriod infoExecutionPeriod, InfoRoom infoRoom, Integer executionPeriodId) {

	if (executionPeriodId == null) {
	    executionPeriodId = infoExecutionPeriod.getIdInternal();
	}

	final AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(infoRoom.getIdInternal());
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	final List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
	for (final Lesson elem : room.getAssociatedLessons(executionSemester)) {
	    if (!elem.hasShift()) {
		continue;
	    }
	    infoAulas.add(InfoLesson.newInfoFromDomain(elem));
	}

	return infoAulas;
    }

}
