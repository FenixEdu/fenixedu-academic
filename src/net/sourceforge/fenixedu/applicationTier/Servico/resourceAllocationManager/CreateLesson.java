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
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.joda.time.YearMonthDay;

public class CreateLesson extends Service {

    public void run(DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency, 
	    InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift,
	    YearMonthDay beginDate, YearMonthDay endDate) throws FenixServiceException {

	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoShift
		.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal());

	final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());

	AllocatableSpace room = null;
	if (infoRoomOccupation != null) {
	    room = infoRoomOccupation.getInfoRoom() != null ? AllocatableSpace.findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome()) : null;	    
	}

	new Lesson(weekDay, begin, end, shift, frequency, executionPeriod, beginDate, endDate, room);	
    }
}
