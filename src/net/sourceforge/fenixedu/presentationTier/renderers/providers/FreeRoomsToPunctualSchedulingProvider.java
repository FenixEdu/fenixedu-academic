package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class FreeRoomsToPunctualSchedulingProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	Set<AllocatableSpace> result = new TreeSet<AllocatableSpace>(AllocatableSpace.ROOM_COMPARATOR_BY_NAME);	
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) source;	
	
	List<AllocatableSpace> selectedRooms = bean.getRooms();	
	Integer frequency = (bean.getFrequency() != null) ? bean.getFrequency().ordinal() + 1 : null;
	YearMonthDay begin = bean.getBegin();
	YearMonthDay end = bean.getEnd();	
	Partial beginTime = bean.getBeginTime();
	Partial endTime = bean.getEndTime();	
	Boolean markSaturday = bean.getMarkSaturday();
	Boolean markSunday = bean.getMarkSunday();
	OccupationPeriod period = OccupationPeriod.readOccupationPeriod(begin, end);	
	DiaSemana diaSemana = new DiaSemana(getDayOfWeek(begin));
	
	HourMinuteSecond startTimeHMS = new HourMinuteSecond(beginTime.get(DateTimeFieldType.hourOfDay()), beginTime.get(DateTimeFieldType.minuteOfHour()), 0);
	HourMinuteSecond endTimeHMS = new HourMinuteSecond(endTime.get(DateTimeFieldType.hourOfDay()), endTime.get(DateTimeFieldType.minuteOfHour()), 0);
	
	for (AllocatableSpace room :  AllocatableSpace.getAllActiveAllocatableSpacesForEducation()) {	    
	    if (!selectedRooms.contains(room)) {				
		if(room.isFree(period.getStartYearMonthDay(), period.getEndYearMonthDay(), 
			startTimeHMS, endTimeHMS, diaSemana, frequency, null, markSaturday, markSunday)) {
		    result.add(room);
		} 
	    } 
	}
				
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
    
    private int getDayOfWeek(YearMonthDay begin) {
	int dayOfWeek = begin.toDateTimeAtMidnight().getDayOfWeek();
	if(dayOfWeek == 7) {
	    return 1;
	}
	return dayOfWeek + 1;
    }
}
