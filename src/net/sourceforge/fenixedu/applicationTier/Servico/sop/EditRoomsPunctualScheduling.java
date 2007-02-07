package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.sop.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTimeFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class EditRoomsPunctualScheduling extends Service {

    public void run(RoomsPunctualSchedulingBean bean) {
	
	List<OldRoom> selectedRooms = bean.getRooms();
	GenericEvent genericEvent = bean.getGenericEvent();
	if (genericEvent != null && !selectedRooms.isEmpty()) {	    
	    
	    // Remove Occupations
	    List<RoomOccupation> roomOccupations = genericEvent.getRoomOccupations();
	    List<RoomOccupation> eventOccupations = new ArrayList<RoomOccupation>();
	    eventOccupations.addAll(roomOccupations);
	    
	    for (RoomOccupation occupation : eventOccupations) {
		if(!selectedRooms.contains(occupation.getRoom())) {
		    occupation.delete();
		} else {
		    selectedRooms.remove(occupation.getRoom());
		}
	    }	
	    
	    // Associate new Occupations
	    if(!selectedRooms.isEmpty()) {
               
		Calendar beginTimeCalendar = bean.getBegin().toDateTime(new TimeOfDay(bean.getBeginTime().get(DateTimeFieldType.hourOfDay()), 
                    bean.getBeginTime().get(DateTimeFieldType.minuteOfHour()), 0))
                    .toCalendar(LanguageUtils.getLocale());
                
                Calendar endTimeCalendar = bean.getEnd().toDateTime(new TimeOfDay(bean.getEndTime().get(DateTimeFieldType.hourOfDay()),
                    bean.getEndTime().get(DateTimeFieldType.minuteOfHour()), 0))
                    .toCalendar(LanguageUtils.getLocale());
                	   
                DiaSemana diaSemana = new DiaSemana(getDayOfWeek(bean.getBegin()));
                OccupationPeriod period = OccupationPeriod.readFor(bean.getBegin(), bean.getEnd()); 
                                
                for (OldRoom room : selectedRooms) {		
                    genericEvent.createNewRoomOccupation(room, beginTimeCalendar, endTimeCalendar, diaSemana, period);		
                }
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
