/* 
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

public class CreateLesson extends Service {

    public InfoLessonServiceResult run(DiaSemana weekDay, Calendar begin, Calendar end,
	    FrequencyType frequency, InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift) throws FenixServiceException {

	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoShift
		.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal());

	final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());

	InfoLessonServiceResult result = validTimeInterval(begin, end);
	if (result.getMessageType() == 1) {
	    throw new InvalidTimeIntervalServiceException();
	}

	if (result.isSUCESS()) {

	    Lesson aula = new Lesson(weekDay, begin, end, shift, frequency, executionPeriod);

	    if (infoRoomOccupation != null) {

		AllocatableSpace sala = infoRoomOccupation.getInfoRoom() != null ? 
		AllocatableSpace.findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome()) : null;

		try {
		    new LessonSpaceOccupation(sala, aula);
		    
		} catch (DomainException e) {
		    throw new InterceptingLessonException();
		}
	    }

	} else {
	    result.setMessageType(2);
	}

	return result;
    }

    private InfoLessonServiceResult validTimeInterval(Calendar begin, Calendar end) {
	InfoLessonServiceResult result = new InfoLessonServiceResult();
	if (begin.getTime().getTime() >= end.getTime().getTime()) {
	    result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
	}
	return result;
    }

    public class InvalidLoadException extends FenixServiceException {	
	InvalidLoadException(String s) {
	    super(s);
	}
    }

    public class InterceptingLessonException extends FenixServiceException {
	private InterceptingLessonException() {
	    super();
	}

	InterceptingLessonException(String s) {
	    super(s);
	}
    }
}
