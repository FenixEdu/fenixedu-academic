package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sop.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTimeFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class CreateRoomsPunctualScheduling extends Service {

    public void run(RoomsPunctualSchedulingBean bean) throws FenixServiceException {

	List<OldRoom> selectedRooms = bean.getRooms();
	
	if (!selectedRooms.isEmpty()) {
	
	    GenericEvent event = new GenericEvent(bean.getSmallDescription(), bean.getCompleteDescription());
	    
	    Calendar beginTimeCalendar = bean.getBegin().toDateTime(new TimeOfDay(bean.getBeginTime().get(DateTimeFieldType.hourOfDay()), 
		    bean.getBeginTime().get(DateTimeFieldType.minuteOfHour()), 0))
		    .toCalendar(LanguageUtils.getLocale());

	    Calendar endTimeCalendar = bean.getEnd().toDateTime(new TimeOfDay(bean.getEndTime().get(DateTimeFieldType.hourOfDay()),
		    bean.getEndTime().get(DateTimeFieldType.minuteOfHour()), 0))
		    .toCalendar(LanguageUtils.getLocale());
	    	   
	    DiaSemana diaSemana = new DiaSemana(getDayOfWeek(bean.getBegin()));
	    OccupationPeriod period = OccupationPeriod.readFor(bean.getBegin(), bean.getEnd());            	  		
	    	    
	    for (OldRoom room : bean.getRooms()) {								
		event.createRoomOccupation(room, beginTimeCalendar, endTimeCalendar, diaSemana, bean.getFrequency(), period);		
	    }
	}
    }
    
    private int getDayOfWeek(YearMonthDay begin) {
	int dayOfWeek = begin.toDateTimeAtMidnight().getDayOfWeek();
	if(dayOfWeek == 7) {
	    return 1;
	}
	return dayOfWeek + 1;
    }
}
