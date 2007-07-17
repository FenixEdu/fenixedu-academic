package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public abstract class EventSpaceOccupation extends EventSpaceOccupation_Base {
    
    public static final Comparator<EventSpaceOccupation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("period.startDate"));	
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
    }
        
    private static int SATURDAY_IN_JODA_TIME = 6, SUNDAY_IN_JODA_TIME = 7;
    
    private static transient Locale locale = LanguageUtils.getLocale();
        
    
    public abstract Boolean getDailyFrequencyMarkSaturday();
    
    public abstract Boolean getDailyFrequencyMarkSunday();
    
    public abstract YearMonthDay getBeginDate();
    
    public abstract YearMonthDay getEndDate();
    
    public abstract HourMinuteSecond getStartTimeDateHourMinuteSecond();
    
    public abstract HourMinuteSecond getEndTimeDateHourMinuteSecond();
        
    public abstract DiaSemana getDayOfWeek();
    
    public abstract FrequencyType getFrequency();
               
    protected EventSpaceOccupation() {
        super();        
    }    
         
    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
        if(!resource.isAllocatableSpace()) {
            throw new DomainException("error.EventSpaceOccupation.invalid.resource");
        }
    }    
              
    @Override
    public boolean isEventSpaceOccupation() {
	return true;
    }    
    
    public AllocatableSpace getRoom() {
	return (AllocatableSpace) getResource();
    }
              
    public Calendar getStartTime() {	
	HourMinuteSecond hms = getStartTimeDateHourMinuteSecond();
	Date date = (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());	
        if (date != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(date);
            return result;
        }
        return null;
    }

    public Calendar getEndTime() {
	HourMinuteSecond hms = getEndTimeDateHourMinuteSecond();
	Date date = (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
	if (date != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(date);
            return result;
        }
        return null;
    }   
        
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
	return !getBeginDate().isAfter(endDate) && !getEndDate().isBefore(startDate);
    }
    
    public boolean alreadyWasOccupiedBy(EventSpaceOccupation occupation) {

	if (this.equals(occupation)) {
	    return true;
	}
	
	if (intersects(occupation.getBeginDate(), occupation.getEndDate())) {                       	   	    	    			   	    
	  
	    List<Interval> thisOccupationIntervals = getEventSpaceOccupationIntervals();
	    List<Interval> passedOccupationIntervals = occupation.getEventSpaceOccupationIntervals();			    			    	 
	    
	    for (Interval interval : thisOccupationIntervals) {		    
                for (Interval passedInterval : passedOccupationIntervals) {
                    if(interval.getStart().isBefore(passedInterval.getEnd()) && interval.getEnd().isAfter(passedInterval.getStart())) {
                        return true;
                    }
                }		    												    
            }
        }	
        return false;
    }        
    
    public boolean alreadyWasOccupiedIn(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime, 
	    HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, 
	    Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {
      		
	startTime.setSecondOfMinute(0);
	endTime.setSecondOfMinute(0);
	
	if (intersects(startDate, endDate)) {                       	   	    	    			   	    
	    
	    List<Interval> thisOccupationIntervals = getEventSpaceOccupationIntervals();
	    List<Interval> passedOccupationIntervals = getEventSpaceOccupationIntervals(startDate, endDate, startTime, endTime,
		    frequency, dayOfWeek, dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday);		
	    			    	  
	    for (Interval interval : thisOccupationIntervals) {		    
                for (Interval passedInterval : passedOccupationIntervals) {
                    if(interval.getStart().isBefore(passedInterval.getEnd()) && interval.getEnd().isAfter(passedInterval.getStart())) {
                        return true;
                    }
                }		    												    
            }
        }	
        return false;
    }
    
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay begin, YearMonthDay end){
	List<Interval> result = new ArrayList<Interval>();
	for (Interval interval : getEventSpaceOccupationIntervals()) {
	    if(!interval.getStart().toYearMonthDay().isAfter(end) && !interval.getEnd().toYearMonthDay().isBefore(begin)) {
		result.add(interval);
	    }
	}
	return result;
    }
    
    public List<Interval> getEventSpaceOccupationIntervals() {
	return getEventSpaceOccupationIntervals(getBeginDate(), getEndDate(), getStartTimeDateHourMinuteSecond(), 
		getEndTimeDateHourMinuteSecond(), getFrequency(), getDayOfWeek(),
		getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday());	
    }     
          
    protected List<Interval> getEventSpaceOccupationIntervals(YearMonthDay begin, YearMonthDay end,
	    HourMinuteSecond beginTime, HourMinuteSecond endTime, FrequencyType frequency, DiaSemana diaSemana, 
	    Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {

	List<Interval> result = new ArrayList<Interval>();		
			
	if(diaSemana != null) {
	    YearMonthDay newBegin = begin.toDateTimeAtMidnight().withDayOfWeek(diaSemana.getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();	    
	    if(newBegin.isBefore(begin)) {
		begin = newBegin.plusDays(7);
	    } else {
		begin = newBegin;
	    }
	}

	if(frequency == null) {            
	    if (!begin.isAfter(end)) {
                result.add(createNewInterval(begin, end, beginTime, endTime));
                return result;
            }            
	} else {
            int numberOfDaysToSum = frequency.getNumberOfDays();            
            while (true) {
                if (begin.isAfter(end)) {
                    break;
                }
                Interval interval = createNewInterval(begin, begin, beginTime, endTime);                              
                if(!frequency.equals(FrequencyType.DAILY) || 
                	((dailyFrequencyMarkSaturday || interval.getStart().getDayOfWeek() != SATURDAY_IN_JODA_TIME) &&
                	(dailyFrequencyMarkSunday || interval.getStart().getDayOfWeek() != SUNDAY_IN_JODA_TIME))) {
                 
                    result.add(interval);   
                }                
                begin = begin.plusDays(numberOfDaysToSum);
            }
	}
	return result; 
    }
                 
    protected Interval createNewInterval(YearMonthDay begin, YearMonthDay end, HourMinuteSecond beginTime, HourMinuteSecond endTime) {	
	return new Interval(
		begin.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0, 0)),			
		end.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0, 0)));
    }     
    
    public DateTime getFirstInstant() {		
	List<Interval> roomOccupationIntervals = getEventSpaceOccupationIntervals();
	return (roomOccupationIntervals != null && !roomOccupationIntervals.isEmpty()) ?
		roomOccupationIntervals.get(0).getStart() : null;	
    }
    
    public DateTime getLastInstant() {	
	List<Interval> roomOccupationIntervals = getEventSpaceOccupationIntervals();
	return (roomOccupationIntervals != null && !roomOccupationIntervals.isEmpty()) ?
		roomOccupationIntervals.get(roomOccupationIntervals.size() - 1).getEnd() : null;	
    }   
           
    public String getPrettyPrint() {
	StringBuilder builder = new StringBuilder();
	if(getFrequency() == null) {
	    builder.append(getBeginDate().toString("dd/MM/yyyy")).append(" ").append(getPresentationBeginTime());
	    builder.append(" - ").append(getEndDate().toString("dd/MM/yyyy")).append(" ").append(getPresentationEndTime());	   	 
	}
	else {
	    builder.append(getBeginDate().toString("dd/MM/yyyy")).append(" - ").append(getEndDate().toString("dd/MM/yyyy"));	    
	    builder.append(" (").append(getPresentationBeginTime()).append(" - ").append(getPresentationEndTime()).append(")");
	}
	return builder.toString();
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
}
