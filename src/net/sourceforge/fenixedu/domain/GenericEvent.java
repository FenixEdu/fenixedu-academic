package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GenericEvent extends GenericEvent_Base implements GanttDiagramEvent {
    
    public static final Comparator<GenericEvent> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"), true);
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginTime"), true);
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
    }
    
    public GenericEvent(MultiLanguageString title, MultiLanguageString description, List<OldRoom> rooms, 
	    YearMonthDay beginDate, YearMonthDay endDate, HourMinuteSecond beginTime, 
	    HourMinuteSecond endTime, FrequencyType frequencyType, PunctualRoomsOccupationRequest request,
	    Boolean markSaturday, Boolean markSunday) {
	
	super();        		
	
	if(rooms.isEmpty()) {
	    throw new DomainException("error.GenericEvent.empty.rooms");
	}		
	
        if(request != null) {  
            if(request.getCurrentState().equals(RequestState.RESOLVED)) {
                throw new DomainException("error.GenericEvent.request.was.resolved");
            }
            request.associateNewGenericEvent(AccessControl.getPerson(), this, new DateTime());
        }
        
        setRootDomainObject(RootDomainObject.getInstance());
        setTitle(title);
        setDescription(description);        
	setFrequency(frequencyType);
        	
	for (OldRoom room : rooms) {	
	    new RoomOccupation(room, beginDate, endDate, beginTime, endTime, frequencyType, this, markSaturday, markSunday);
	}
    }  
    
    public void edit(MultiLanguageString title, MultiLanguageString description, List<OldRoom> newRooms, List<RoomOccupation> roomOccupationsToRemove) {	
		
	if(getPunctualRoomsOccupationRequest() != null && 
		getPunctualRoomsOccupationRequest().getCurrentState().equals(RequestState.RESOLVED)) {
            throw new DomainException("error.GenericEvent.request.was.resolved");
        }		
	
	setTitle(title);
	setDescription(description);	
	
	YearMonthDay beginDate = getBeginDate();
	YearMonthDay endDate = getEndDate();
	HourMinuteSecond beginTime = getBeginTime();
	HourMinuteSecond endTime = getEndTime();
	FrequencyType frequency = getFrequency();
	Boolean markSaturday = getDailyFrequencyMarkSaturday();
	Boolean markSunday = getDailyFrequencyMarkSunday();
	
	while(!roomOccupationsToRemove.isEmpty()) {
            roomOccupationsToRemove.remove(0).delete();
        }
	
	for (OldRoom room : newRooms) {	
	    new RoomOccupation(room, beginDate, endDate, beginTime, endTime, frequency, this, markSaturday, markSunday);
        }	
	
	if(getRoomOccupations().isEmpty()) {
	    throw new DomainException("error.GenericEvent.empty.rooms");
	}
    }       
    
    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    throw new DomainException("error.genericCalendar.empty.title");
	}
	super.setTitle(title);
    }
    
    public void delete() {	
	
	if(getLastInstant().isBeforeNow()) {
	    throw new DomainException("error.GenericEvent.impossible.delete.because.was.old.event");
	}	
	if(getPunctualRoomsOccupationRequest() != null && getPunctualRoomsOccupationRequest().getCurrentState().equals(RequestState.RESOLVED)) {
            throw new DomainException("error.GenericEvent.request.was.resolved");
        }
	
	while(hasAnyRoomOccupations()) {
	    getRoomOccupations().get(0).delete();
	}
	
	removePunctualRoomsOccupationRequest();
	removeRootDomainObject();
	deleteDomainObject();
    }
              
    public static Set<GenericEvent> getActiveGenericEventsForRoomOccupations(){	
	Set<GenericEvent> result = new TreeSet<GenericEvent>(COMPARATOR_BY_BEGIN_DATE);
	for (GenericEvent genericEvent : RootDomainObject.getInstance().getGenericEvents()) {
	    if(genericEvent.isActive()) {
		result.add(genericEvent);
	    }
	}
	return result;
    }
    
    public boolean isActive() {
	DateTime lastInstant = getLastInstant();
	return (lastInstant == null) || (lastInstant != null && !lastInstant.isBeforeNow()) ? true : false;
    }

    public List<OldRoom> getAssociatedRooms(){
	List<OldRoom> result = new ArrayList<OldRoom>();
	for (RoomOccupation occupation : getRoomOccupationsSet()) {
	    result.add(occupation.getRoom());	
	}
	return result;
    }
    
    public DiaSemana getWeekDay() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getDayOfWeek() : null;
    }
    
    public Boolean getDailyFrequencyMarkSaturday() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getDailyFrequencyMarkSaturday() : null;
    }
    
    public Boolean getDailyFrequencyMarkSunday() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getDailyFrequencyMarkSunday() : null;
    }
    
    public DateTime getLastInstant() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getLastInstant() : null;
    }
    
    public OccupationPeriod getOccupationPeriod() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPeriod() : null;
    }
    
    public YearMonthDay getBeginDate() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPeriod().getStartYearMonthDay() : null;
    }
    
    public YearMonthDay getEndDate() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getPeriod().getEndYearMonthDay() : null;
    }
    
    public Calendar getBeginTimeCalendar() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getStartTime() : null;
    }
    
    public Calendar getEndTimeCalendar() {
	return (!getRoomOccupations().isEmpty()) ? getRoomOccupations().get(0).getEndTime() : null;
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
		String saturday = "", sunday = "", marker = "";		
		if(getFrequency().equals(FrequencyType.DAILY)) {
		    saturday = getDailyFrequencyMarkSaturday() ? "S" : "";
		    sunday = getDailyFrequencyMarkSunday() ? "D" : "";
		    if(getDailyFrequencyMarkSaturday() || getDailyFrequencyMarkSunday()) {
			marker = "-";
		    }
		}			
		return "[" + getFrequency().getAbbreviation() + marker + saturday + sunday + "] " + prettyPrint;
	    }
	    return "[C] " + prettyPrint;
	}	
	return " - ";
    }      
}
