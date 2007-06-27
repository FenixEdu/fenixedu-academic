package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam extends Service {

    public List<InfoRoom> run(Calendar periodStart, Calendar periodEnd, Calendar startTime, Calendar endTime,
	    DiaSemana dayOfWeek, Integer roomOccupationToRemoveId, Integer normalCapacity,
	    Integer frequency, Integer weekOfStart, Boolean withLabs) throws ExcepcaoPersistencia {

	final Collection<AllocatableSpace> rooms = new HashSet<AllocatableSpace>();
	final List<AllocatableSpace> roomsToCheck = new ArrayList<AllocatableSpace>();
	
	if (normalCapacity != null) {
	    roomsToCheck.addAll(AllocatableSpace.findActiveAllocatableSpacesForEducationWithNormalCapacity(normalCapacity));
	
	} else if (withLabs.booleanValue()) {
	    roomsToCheck.addAll(AllocatableSpace.getAllActiveAllocatableSpacesForEducation());
	
	} else {
	    roomsToCheck.addAll(AllocatableSpace.getAllActiveAllocatableSpacesExceptLaboratoriesForEducation());
	}

	YearMonthDay startDate = YearMonthDay.fromCalendarFields(periodStart);
	YearMonthDay endDate = YearMonthDay.fromCalendarFields(periodEnd);
	HourMinuteSecond startTimeHMS = HourMinuteSecond.fromCalendarFields(startTime);
	HourMinuteSecond endTimeHMS = HourMinuteSecond.fromCalendarFields(endTime);	
	
	for (AllocatableSpace allocatableSpace : roomsToCheck) {
	    if(allocatableSpace.isFree(startDate, endDate, startTimeHMS, endTimeHMS, dayOfWeek,
		    frequency, weekOfStart, Boolean.TRUE, Boolean.TRUE)) {
		rooms.add(allocatableSpace);
	    }
	}
   
	final List<InfoRoom> availableInfoRooms = new ArrayList<InfoRoom>();
	for (final AllocatableSpace room : rooms) {
	    if(room.containsIdentification()) {
		availableInfoRooms.add(InfoRoom.newInfoFromDomain(room));	    
	    }
	}

	return availableInfoRooms;
    } 
}