package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GenericEvent extends GenericEvent_Base implements GanttDiagramEvent {
    
    public static final Comparator<GenericEvent> COMPARATOR_BY_DATE_AND_TIME = new ComparatorChain();   
    private static transient Locale locale = LanguageUtils.getLocale();
    
    static {
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("endDate"));
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("endTimeDateHourMinuteSecond"));
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("beginDate"));
	((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("startTimeDateHourMinuteSecond"));	
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
        
        if(frequencyType != null && frequencyType.equals(FrequencyType.DAILY) && (markSunday == null || markSaturday == null)) {
            throw new DomainException("error.GenericEvent.invalid.dailyFrequency");
        }
        
        setRootDomainObject(RootDomainObject.getInstance());
        setTitle(title);
        setDescription(description);        
	setFrequency(frequencyType);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setStartTimeDateHourMinuteSecond(beginTime);
	setEndTimeDateHourMinuteSecond(endTime);
	setDailyFrequencyMarkSunday(markSunday);
	setDailyFrequencyMarkSaturday(markSaturday);
        	
	for (AllocatableSpace allocatableSpace : allocatableSpaces) {	
	    new GenericEventSpaceOccupation(allocatableSpace, this);
	}
	
	if(!hasAnyGenericEventSpaceOccupations()) {
	    throw new DomainException("error.GenericEvent.empty.rooms");
	}
    }  
    
    public void edit(MultiLanguageString title, MultiLanguageString description, List<AllocatableSpace> newRooms, List<GenericEventSpaceOccupation> roomOccupationsToRemove) {	
		
	if(getPunctualRoomsOccupationRequest() != null && !getPunctualRoomsOccupationRequest().getCurrentState().equals(RequestState.NEW)) {
            throw new DomainException("error.edit.GenericEvent.request.was.resolved.or.opened");
        }		
		
	setTitle(title);
	setDescription(description);	
	
	while(!roomOccupationsToRemove.isEmpty()) {
            roomOccupationsToRemove.remove(0).delete();
        }
	
	for (AllocatableSpace room : newRooms) {	
	    new GenericEventSpaceOccupation(room, this);
        }	
	
	if(!hasAnyGenericEventSpaceOccupations()) {
	    throw new DomainException("error.GenericEvent.empty.rooms");
	}
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
           
    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    throw new DomainException("error.genericCalendar.empty.title");
	}
	super.setTitle(title);
    }
    
    @Override
    public void setBeginDate(YearMonthDay beginDate) {
        if(beginDate == null) {
            throw new DomainException("error.GenericEvent.empty.beginDate");
        }
	super.setBeginDate(beginDate);
    }
    
    @Override
    public void setEndDate(YearMonthDay endDate) {
	if(endDate == null) {
	    throw new DomainException("error.GenericEvent.empty.endDate");
	}
	super.setEndDate(endDate);
    }
    
    @Override
    public void setStartTimeDateHourMinuteSecond(HourMinuteSecond startTimeDateHourMinuteSecond) {
        if(startTimeDateHourMinuteSecond == null) {
            throw new DomainException("error.GenericEvent.empty.startTime");
        }
	super.setStartTimeDateHourMinuteSecond(startTimeDateHourMinuteSecond);
    }
    
    @Override
    public void setEndTimeDateHourMinuteSecond(HourMinuteSecond endTimeDateHourMinuteSecond) {
	if(endTimeDateHourMinuteSecond == null) {
	    throw new DomainException("error.GenericEvent.empty.endTime");
	}
	super.setEndTimeDateHourMinuteSecond(endTimeDateHourMinuteSecond);
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
    
    public boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
	return !getBeginDate().isAfter(endDate) && !getEndDate().isBefore(startDate);
    }
             
    public DateTime getLastInstant() {
	return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getLastInstant() : null;
    }
           
    public Calendar getBeginTimeCalendar() {
	 Calendar result = Calendar.getInstance();
         result.setTime(getStartTimeDate());
         return result;
    }
    
    public Calendar getEndTimeCalendar() {
	Calendar result = Calendar.getInstance();
        result.setTime(getEndTimeDate());
        return result;
    }
        
    public String getPresentationBeginTime() {
	return getStartTimeDateHourMinuteSecond().toString("HH:mm");
    }
    
    public String getPresentationEndTime() {
	return getEndTimeDateHourMinuteSecond().toString("HH:mm");
    }
    
    public String getPresentationBeginDate() {
	return getBeginDate().toString("dd MMMM yyyy", locale) + " (" + getBeginDate().toDateTimeAtMidnight().toString("E", locale) + ")";
    }
    
    public String getPresentationEndDate() {
	return getEndDate().toString("dd MMMM yyyy", locale) + " (" + getEndDate().toDateTimeAtMidnight().toString("E", locale) + ")";
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

    private boolean intersectPeriod(DateTime firstDayOfMonth, DateTime lastDayOfMonth) {		
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
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateTimeIntervals() {	
	return getStartTimeDateHourMinuteSecond() != null && getEndTimeDateHourMinuteSecond() != null		
	 	&& getStartTimeDateHourMinuteSecond().isBefore(getEndTimeDateHourMinuteSecond()) 
		&& getBeginDate() != null && getEndDate() != null
		&& !getBeginDate().isAfter(getEndDate());
    }
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return getTitle() != null && !getTitle().isEmpty()
		&& (getFrequency() == null || !getFrequency().equals(FrequencyType.DAILY) || 
			(getDailyFrequencyMarkSaturday() != null && getDailyFrequencyMarkSunday() != null));	
    }
}
