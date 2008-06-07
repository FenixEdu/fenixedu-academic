package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FreeRoomsToPunctualSchedulingProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	Set<AllocatableSpace> result = new TreeSet<AllocatableSpace>(AllocatableSpace.ROOM_COMPARATOR_BY_NAME);	
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) source;	
	
	List<AllocatableSpace> selectedRooms = bean.getRooms();	
	FrequencyType frequency = bean.getFrequency();
	YearMonthDay beginDate = bean.getBegin();
	YearMonthDay endDate = bean.getEnd();	
	Partial beginTime = bean.getBeginTime();
	Partial endTime = bean.getEndTime();	
	Boolean markSaturday = bean.getMarkSaturday();
	Boolean markSunday = bean.getMarkSunday();
	DiaSemana diaSemana = new DiaSemana(DiaSemana.getDiaSemana(beginDate));
	
	HourMinuteSecond startTimeHMS = new HourMinuteSecond(beginTime.get(DateTimeFieldType.hourOfDay()), beginTime.get(DateTimeFieldType.minuteOfHour()), 0);
	HourMinuteSecond endTimeHMS = new HourMinuteSecond(endTime.get(DateTimeFieldType.hourOfDay()), endTime.get(DateTimeFieldType.minuteOfHour()), 0);
	
	for (AllocatableSpace room :  AllocatableSpace.getAllActiveAllocatableSpacesForEducationAndPunctualOccupations()) {	    
	    if (!selectedRooms.contains(room)) {				
		if(room.isFree(beginDate, endDate, startTimeHMS, endTimeHMS, diaSemana, frequency, markSaturday, markSunday)) {
		    result.add(room);
		} 
	    } 
	}
				
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
