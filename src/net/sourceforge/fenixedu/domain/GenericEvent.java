package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
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
    
    public static final Comparator<GenericEvent> COMPARATOR_BY_DATE_AND_TIME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("endDate"));
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("endTime"));
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("beginDate"));
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("beginTime"));	
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    public GenericEvent(MultiLanguageString title, MultiLanguageString description, List<AllocatableSpace> allocatableSpaces, 
	    YearMonthDay beginDate, YearMonthDay endDate, HourMinuteSecond beginTime, 
	    HourMinuteSecond endTime, FrequencyType frequencyType, PunctualRoomsOccupationRequest request,
	    Boolean markSaturday, Boolean markSunday) {
	
	super();        		
	
	if(allocatableSpaces.isEmpty()) {
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
        	
	for (AllocatableSpace allocatableSpace : allocatableSpaces) {	
	    new GenericEventSpaceOccupation(allocatableSpace, beginDate, endDate, beginTime, endTime, frequencyType, this, markSaturday, markSunday);
	}
    }  
    
    public void edit(MultiLanguageString title, MultiLanguageString description, List<AllocatableSpace> newRooms, List<GenericEventSpaceOccupation> roomOccupationsToRemove) {	
		
	if(getPunctualRoomsOccupationRequest() != null && !getPunctualRoomsOccupationRequest().getCurrentState().equals(RequestState.NEW)) {
            throw new DomainException("error.edit.GenericEvent.request.was.resolved.or.opened");
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
	
	for (AllocatableSpace room : newRooms) {	
	    new GenericEventSpaceOccupation(room, beginDate, endDate, beginTime, endTime, frequency, this, markSaturday, markSunday);
        }	
	
	if(getGenericEventSpaceOccupations().isEmpty()) {
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
	
	while(hasAnyGenericEventSpaceOccupations()) {
	    getGenericEventSpaceOccupations().get(0).delete();
	}
	
	removePunctualRoomsOccupationRequest();
	removeRootDomainObject();
	deleteDomainObject();
    }
              
    public static Set<GenericEvent> getActiveGenericEventsForRoomOccupations(){	
	Set<GenericEvent> result = new TreeSet<GenericEvent>(COMPARATOR_BY_DATE_AND_TIME);
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

    public List<AllocatableSpace> getAssociatedRooms(){
	List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	for (GenericEventSpaceOccupation occupation : getGenericEventSpaceOccupations()) {
	    result.add(occupation.getRoom());	
	}
	return result;
    }
    
    public List<Interval> getGenericEventIntervals(YearMonthDay begin, YearMonthDay end){
	if(!getGenericEventSpaceOccupations().isEmpty()) {
	    GenericEventSpaceOccupation occupation = getGenericEventSpaceOccupations().get(0);
	    return occupation.getEventSpaceOccupationIntervals(begin, end);	    
	}
	return new ArrayList<Interval>();
    }
           
    public DiaSemana getWeekDay() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getDayOfWeek() : null;
    }
    
    public Boolean getDailyFrequencyMarkSaturday() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getDailyFrequencyMarkSaturday() : null;
    }
    
    public Boolean getDailyFrequencyMarkSunday() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getDailyFrequencyMarkSunday() : null;
    }
    
    public DateTime getLastInstant() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getLastInstant() : null;
    }
    
    public OccupationPeriod getOccupationPeriod() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPeriod() : null;
    }
    
    public YearMonthDay getBeginDate() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPeriod().getStartYearMonthDay() : null;
    }
    
    public YearMonthDay getEndDate() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPeriod().getEndYearMonthDay() : null;
    }
    
    public Calendar getBeginTimeCalendar() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getStartTime() : null;
    }
    
    public Calendar getEndTimeCalendar() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getEndTime() : null;
    }
    
    public HourMinuteSecond getBeginTime() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getStartTimeDateHourMinuteSecond() : null;
    }
    
    public HourMinuteSecond getEndTime() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getEndTimeDateHourMinuteSecond() : null;
    }
    
    public String getPresentationBeginTime() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPresentationBeginTime() : " - ";
    }
    
    public String getPresentationEndTime() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPresentationEndTime() : " - ";
    }
    
    public String getPresentationBeginDate() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPresentationBeginDate() : " - ";
    }
    
    public String getPresentationEndDate() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getPresentationEndDate() : " - ";
    }
  
    public String getGanttDiagramEventIdentifier() {
	return getIdInternal().toString();	
    }

    public MultiLanguageString getGanttDiagramEventName() {	
	return getTitle();
    }

    public String getGanttDiagramEventObservations() {
	StringBuilder builder = new StringBuilder();
	for (GenericEventSpaceOccupation roomOccupation : getGenericEventSpaceOccupations()) {
	    builder.append(" ").append(roomOccupation.getRoom().getName());
	}
	return builder.toString();
    }

    public int getGanttDiagramEventOffset() {	
	return 0;
    }

    public List<Interval> getGanttDiagramEventSortedIntervals() {		
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getEventSpaceOccupationIntervals() : new ArrayList<Interval>();
    }

    public String getGanttDiagramEventPeriod() {
	if(!getGenericEventSpaceOccupations().isEmpty()) {
	    String prettyPrint = getGenericEventSpaceOccupations().get(0).getPrettyPrint();	    
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

    public boolean intersectPeriod(DateTime firstDayOfMonth, DateTime lastDayOfMonth) {		
        for (Interval interval : getGanttDiagramEventSortedIntervals()) {
            if(interval.getStart().isAfter(lastDayOfMonth)) {
                return false;
            }
            if(interval.getStart().isBefore(lastDayOfMonth) && interval.getEnd().isAfter(firstDayOfMonth)) {
                return true;
            }
        }
	return false;
    }
    
    public static Set<GenericEvent> getAllGenericEvents(DateTime begin, DateTime end){
	Set<GenericEvent> events = new TreeSet<GenericEvent>(GenericEvent.COMPARATOR_BY_DATE_AND_TIME);
	for (GenericEvent genericEvent : RootDomainObject.getInstance().getGenericEvents()) {
	    if(genericEvent.intersectPeriod(begin, end)) {
		events.add(genericEvent);
	    }
	}	    	   
	return events;
    }
}
