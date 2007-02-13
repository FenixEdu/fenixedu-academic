package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.sop.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class FreeRoomsToPunctualSchedulingProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	Set<OldRoom> result = new TreeSet<OldRoom>(OldRoom.OLD_ROOM_COMPARATOR_BY_NAME);	
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) source;	
	
	List<OldRoom> selectedRooms = bean.getRooms();	
	Integer frequency = (bean.getFrequency() != null) ? bean.getFrequency().ordinal() + 1 : null;
	YearMonthDay begin = bean.getBegin();
	YearMonthDay end = bean.getEnd();	
	Partial beginTime = bean.getBeginTime();
	Partial endTime = bean.getEndTime();	
	OccupationPeriod period = OccupationPeriod.readFor(begin, end);	
	DiaSemana diaSemana = new DiaSemana(getDayOfWeek(begin));
	
	Calendar beginTimeCalendar = begin.toDateTime(new TimeOfDay(beginTime.get(DateTimeFieldType.hourOfDay()),
		beginTime.get(DateTimeFieldType.minuteOfHour()), 0)).toCalendar(LanguageUtils.getLocale());

	Calendar endTimeCalendar = end.toDateTime(new TimeOfDay(endTime.get(DateTimeFieldType.hourOfDay()),
		endTime.get(DateTimeFieldType.minuteOfHour()), 0)).toCalendar(LanguageUtils.getLocale());
	
	for (OldRoom room : OldRoom.getOldRooms()) {	    
	    if (!selectedRooms.contains(room)) {				
		if(room.isFree(period, beginTimeCalendar, endTimeCalendar, diaSemana, frequency, null)) {
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
