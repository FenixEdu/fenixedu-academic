package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.joda.time.YearMonthDay;

public class EditLesson extends Service {

    public Object run(InfoLesson aulaAntiga, DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency, 
	    InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay newBeginDate, 
	    YearMonthDay newEndDate, Boolean createLessonInstances) throws FenixServiceException {

	InfoLessonServiceResult result = null;
	Lesson aula = rootDomainObject.readLessonByOID(aulaAntiga.getIdInternal());

	if (aula != null) {

	    AllocatableSpace salaNova = null;

	    if(infoRoomOccupation != null && infoRoomOccupation.getInfoRoom() != null) {
		salaNova = AllocatableSpace.findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome());
	    }

	    result = valid(begin, end);
	    if (result.getMessageType() == 1) {
		throw new InvalidTimeIntervalServiceException();
	    }

	    if (result.isSUCESS()) {
		        
		aula.edit(newBeginDate, newEndDate, weekDay, begin, end, frequency, createLessonInstances);                                            
		LessonSpaceOccupation lessonSpaceOccupation = aula.getLessonSpaceOccupation();
		
		if(salaNova != null) {
		    if(!aula.wasFinished()) {
			try {                
			    if(lessonSpaceOccupation == null) {                	                    	                    
				lessonSpaceOccupation = new LessonSpaceOccupation(salaNova, aula);   
			    } else {
				lessonSpaceOccupation.edit(salaNova);
			    }
			} catch (DomainException e) {
			    throw new InterceptingServiceException(e);
			}
		    }
		} else {
		    if(lessonSpaceOccupation != null) {
			lessonSpaceOccupation.delete();
		    }
		}
	    }
	}

	return result;
    }

    private InfoLessonServiceResult valid(Calendar start, Calendar end) {
	InfoLessonServiceResult result = new InfoLessonServiceResult();
	if (start.getTime().getTime() >= end.getTime().getTime()) {
	    result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
	}
	return result;
    }

    public class InvalidLoadException extends FenixServiceException {       
	InvalidLoadException(String s) {
	    super(s);
	}
    }
}
