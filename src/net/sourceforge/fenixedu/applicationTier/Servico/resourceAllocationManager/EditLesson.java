package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.joda.time.YearMonthDay;

public class EditLesson extends Service {

    public void run(InfoLesson aulaAntiga, DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency,
	    InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay newBeginDate, YearMonthDay newEndDate,
	    Boolean createLessonInstances) throws FenixServiceException {

	Lesson aula = rootDomainObject.readLessonByOID(aulaAntiga.getIdInternal());

	if (aula != null) {

	    AllocatableSpace newRoom = null;
	    if (infoRoomOccupation != null && infoRoomOccupation.getInfoRoom() != null) {
		newRoom = AllocatableSpace.findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome());
	    }

	    aula.edit(newBeginDate, newEndDate, weekDay, begin, end, frequency, createLessonInstances, newRoom);
	}
    }
}
