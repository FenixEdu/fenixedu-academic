package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.joda.time.Interval;

public class GenericEvent extends GenericEvent_Base implements GanttDiagramEvent {
    
    public GenericEvent(MultiLanguageString title, MultiLanguageString description) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setTitle(title);
        setDescription(description);        
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
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    public void createRoomOccupation(OldRoom room, Calendar startTime, Calendar endTime, 
	    DiaSemana dayOfWeek, FrequencyType frequencyType, OccupationPeriod occupationPeriod) {
	
	Integer frequency = (frequencyType != null) ? frequencyType.ordinal() + 1 : null;
	if(!room.isFree(occupationPeriod, startTime, endTime, dayOfWeek, frequency, 1)) {
	    throw new DomainException("error.GenericEvent.roomOccupation.room.is.not.free");
	}
	new RoomOccupation(room, startTime, endTime, dayOfWeek, frequencyType, this, occupationPeriod);
    }
    
    public static Set<GenericEvent> getActiveGenericEventsForRoomOccupations(){
	Set<RoomOccupation> punctualRoomOccupations = RoomOccupation.getActivePunctualRoomOccupations();
	Set<GenericEvent> result = new HashSet<GenericEvent>();
	for (RoomOccupation occupation : punctualRoomOccupations) {
	    result.add(occupation.getGenericEvent());
	}
	return result;
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
	    builder.append("[").append(roomOccupation.getRoom().getName()).append("] ");
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
		return prettyPrint + " [" + getFrequency().getAbbreviation() + "]";
	    }
	    return prettyPrint + " [C]";
	}	
	return " - ";
    }
}
