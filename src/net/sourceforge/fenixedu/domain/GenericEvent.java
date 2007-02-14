package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GenericEvent extends GenericEvent_Base implements GanttDiagramEvent {
    
    public GenericEvent(MultiLanguageString title, MultiLanguageString description, List<OldRoom> rooms, 
	    Calendar startTime, Calendar endTime, DiaSemana dayOfWeek, FrequencyType frequencyType, 
	    OccupationPeriod occupationPeriod, PunctualRoomsOccupationRequest request) {
	
	super();        		
	if(rooms.isEmpty()) {
	    throw new DomainException("error.empty.room.to.create.room.occupation");
	}	
	
	setRootDomainObject(RootDomainObject.getInstance());
        setTitle(title);
        setDescription(description);        
	setFrequency(frequencyType);
        
        if(request != null) {
           request.associateNewGenericEvent(AccessControl.getPerson(), this, new DateTime());
        }
        
	for (OldRoom room : rooms) {	
	    createNewRoomOccupation(room, startTime, endTime, dayOfWeek, occupationPeriod);
	}
    }  

    public void createNewRoomOccupation(OldRoom room, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek, 
	    OccupationPeriod occupationPeriod) {
	
	Integer frequency = (getFrequency() != null) ? getFrequency().ordinal() + 1 : null;
	if(!room.isFree(occupationPeriod, startTime, endTime, dayOfWeek, frequency, 1)) {
            throw new DomainException("error.GenericEvent.roomOccupation.room.is.not.free");
        }            
        new RoomOccupation(room, startTime, endTime, dayOfWeek, getFrequency(), this, occupationPeriod);	
    }
    
    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    throw new DomainException("error.genericCalendar.empty.title");
	}
	super.setTitle(title);
    }
    
    public void delete() {
	while(hasAnyRoomOccupations()) {
	    getRoomOccupations().get(0).delete();
	}
	removePunctualRoomsOccupationRequest();
	removeRootDomainObject();
	deleteDomainObject();
    }
              
    public static Set<GenericEvent> getActiveGenericEventsForRoomOccupations(){
	Set<RoomOccupation> punctualRoomOccupations = RoomOccupation.getActivePunctualRoomOccupations();
	Set<GenericEvent> result = new HashSet<GenericEvent>();
	for (RoomOccupation occupation : punctualRoomOccupations) {
	    result.add(occupation.getGenericEvent());
	}
	return result;
    }
    
    public List<OldRoom> getAssociatedRooms(){
	List<OldRoom> result = new ArrayList<OldRoom>();
	for (RoomOccupation occupation : getRoomOccupationsSet()) {
	    result.add(occupation.getRoom());	
	}
	return result;
    }
    
    public YearMonthDay getBeginDate() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPeriod().getStartYearMonthDay() : null;
    }
    
    public YearMonthDay getEndDate() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPeriod().getEndYearMonthDay() : null;
    }
    
    public HourMinuteSecond getBeginTime() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getStartTimeDateHourMinuteSecond() : null;
    }
    
    public HourMinuteSecond getEndTime() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getEndTimeDateHourMinuteSecond() : null;
    }
    
    public String getPresentationBeginTime() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPresentationBeginTime() : " - ";
    }
    
    public String getPresentationEndTime() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPresentationEndTime() : " - ";
    }
    
    public String getPresentationBeginDate() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPresentationBeginDate() : " - ";
    }
    
    public String getPresentationEndDate() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPresentationEndDate() : " - ";
    }
  
    public String getEventIdentifierForGanttDiagram() {
	return getIdInternal().toString();	
    }

    public MultiLanguageString getEventNameForGanttDiagram() {	
	return getTitle();
    }

    public String getEventObservationsForGanttDiagram() {
	StringBuilder builder = new StringBuilder();
	for (RoomOccupation roomOccupation : getRoomOccupations()) {
	    builder.append(" ").append(roomOccupation.getRoom().getName());
	}
	return builder.toString();
    }

    public int getEventOffsetForGanttDiagram() {	
	return 0;
    }

    public List<Interval> getEventSortedIntervalsForGanttDiagram() {		
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getRoomOccupationIntervals() : new ArrayList<Interval>();
    }

    public String getEventPeriodForGanttDiagram() {
	if(!getRoomOccupations().isEmpty()) {
	    String prettyPrint = getRoomOccupations().get(0).getPrettyPrint();
	    if(getFrequency() != null) {
		return "[" + getFrequency().getAbbreviation() + "] " + prettyPrint;
	    }
	    return "[C] " + prettyPrint;
	}	
	return " - ";
    }
}
