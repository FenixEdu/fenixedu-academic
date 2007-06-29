package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.util.CalendarUtil;
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
    
    public static final int DIARIA = 1;

    public static final int SEMANAL = 2;

    public static final int QUINZENAL = 3;      
    
    private static int SATURDAY_IN_JODA_TIME = 6;
   
    private static int SUNDAY_IN_JODA_TIME = 7;
    
    private static transient Locale locale = LanguageUtils.getLocale();
    
    
    public abstract void setFrequency(Integer frequency);
    
    public abstract void setFrequency(FrequencyType type);
    
    public abstract Integer getFrequency();
    
    public abstract Integer getWeekOfQuinzenalStart();
    
    public abstract void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart);
    
    
    protected EventSpaceOccupation() {
        super();        
    }    
           
    public void delete() {
	final OccupationPeriod period = getPeriod();        
        super.setPeriod(null);
        if (period != null) {
            period.delete();
        }
        super.delete();        
    }
    
    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
        if(!resource.isAllocatableSpace()) {
            throw new DomainException("error.EventSpaceOccupation.invalid.resource");
        }
    }
    
    @Override
    public void setDayOfWeek(DiaSemana dayOfWeek) {
        if(dayOfWeek == null) {
            throw new DomainException("error.EventSpaceOccupation.empty.dayOfWeek");
        }
	super.setDayOfWeek(dayOfWeek);
    }
    
    @Override
    public void setPeriod(OccupationPeriod period) {
        if(period == null) {
            throw new DomainException("error.EventSpaceOccupation.empty.period");
        }
	super.setPeriod(period);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateTimeIntervals() {
	final HourMinuteSecond start = getStartTimeDateHourMinuteSecond();
	final HourMinuteSecond end = getEndTimeDateHourMinuteSecond();	
	return start != null && end != null && start.isBefore(end);
    }

    @Override
    public boolean isEventSpaceOccupation() {
	return true;
    }    
    
    public AllocatableSpace getRoom() {
	return getResource().isAllocatableSpace() ? (AllocatableSpace) getResource() : null;
    }
    
    public Calendar getStartTime() {
        if (this.getStartTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStartTimeDate());
            return result;
        }
        return null;
    }

    public Calendar getEndTime() {
        if (this.getEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndTimeDate());
            return result;
        }
        return null;
    }   
    
    public boolean alreadyWasOccupied(YearMonthDay startDate, YearMonthDay endDate, 
	    HourMinuteSecond startTime, HourMinuteSecond endTime, 
	    DiaSemana dayOfWeek, Integer frequency, Integer week, 
	    Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {
      		
	startTime.setSecondOfMinute(0);
	endTime.setSecondOfMinute(0);
	
	if (getPeriod().nestedOccupationPeriodsIntersectDates(startDate, endDate)) {                       	   	    	    			   
	    
	    List<Interval> thisOccupationIntervals = getEventSpaceOccupationIntervals();
	    List<Interval> passedOccupationIntervals = getEventSpaceOccupationIntervals(startDate, endDate, startTime, endTime,
		    frequency, week, dayOfWeek, dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday);		
	    			    	  
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
	    if(interval.getStart().toYearMonthDay().isAfter(end)) {
		break;	    
	    } else if(!interval.getStart().toYearMonthDay().isAfter(end) && !interval.getEnd().toYearMonthDay().isBefore(begin)) {
		result.add(interval);
	    }
	}
	return result;
    }
    
    public List<Interval> getEventSpaceOccupationIntervals() {		
	List<Interval> result = new ArrayList<Interval>();
	OccupationPeriod occupationPeriod = getPeriod();        
	
	result.addAll(getEventSpaceOccupationIntervals(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay(), 
    	    getStartTimeDateHourMinuteSecond(), getEndTimeDateHourMinuteSecond(), getFrequency(), getWeekOfQuinzenalStart(), 
    	    getDayOfWeek(), getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday()));
	                             		           
        while(occupationPeriod.getNextPeriod() != null) {
            result.addAll(getEventSpaceOccupationIntervals(occupationPeriod.getNextPeriod().getStartYearMonthDay(), 
        	    occupationPeriod.getNextPeriod().getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(),
        	    getEndTimeDateHourMinuteSecond(), getFrequency(), getWeekOfQuinzenalStart(), getDayOfWeek(),
        	    getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday()));
            
            occupationPeriod = occupationPeriod.getNextPeriod();             
        }
                
	return result;            		
    }	
          
    private List<Interval> getEventSpaceOccupationIntervals(YearMonthDay begin, YearMonthDay end,
	    HourMinuteSecond beginTime, HourMinuteSecond endTime, Integer frequency, Integer startWeek, DiaSemana diaSemana, 
	    Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {

	List<Interval> result = new ArrayList<Interval>();		
	if (startWeek != null && startWeek.intValue() > 0) {
	    begin = begin.plusDays((startWeek - 1) * 7);	    
	}	
	if(diaSemana != null) {
	    begin = begin.toDateTimeAtMidnight().withDayOfWeek(diaSemana.getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();
	}

	if(frequency == null) {            
	    if (!begin.isAfter(end)) {
                result.add(createNewInterval(begin, end, beginTime, endTime));
                return result;
            }            
	} else {
            int numberOfDaysToSum = (frequency.intValue() == 1) ? 1 : (frequency.intValue() - 1) * 7;            
            while (true) {
                if (begin.isAfter(end)) {
                    break;
                }
                Interval interval = createNewInterval(begin, begin, beginTime, endTime);                              
                if(frequency.intValue() != DIARIA || 
                	((dailyFrequencyMarkSaturday || interval.getStart().getDayOfWeek() != SATURDAY_IN_JODA_TIME) &&
                	(dailyFrequencyMarkSunday || interval.getStart().getDayOfWeek() != SUNDAY_IN_JODA_TIME))) {
                 
                    result.add(interval);   
                }                
                begin = begin.plusDays(numberOfDaysToSum);
            }
	}
	return result; 
    }
             
    private Interval createNewInterval(YearMonthDay begin, YearMonthDay end, HourMinuteSecond beginTime, HourMinuteSecond endTime) {	
	return new Interval(
		begin.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0, 0)),			
		end.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0, 0)));
    }     
    
    public String getPrettyPrint() {
	StringBuilder builder = new StringBuilder();
	if(getFrequency() == null) {
	    builder.append(getPeriod().getStartYearMonthDay().toString("dd/MM/yyyy")).append(" ").append(getPresentationBeginTime());
	    builder.append(" - ").append(getPeriod().getEndYearMonthDay().toString("dd/MM/yyyy")).append(" ").append(getPresentationEndTime());	   	 
	}
	else {
	    builder.append(getPeriod().getStartYearMonthDay().toString("dd/MM/yyyy")).append(" - ").append(getPeriod().getEndYearMonthDay().toString("dd/MM/yyyy"));	    
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
	return getPeriod().getStartYearMonthDay().toString("dd MMMM yyyy", locale) + " (" + getPeriod().getStartYearMonthDay().toDateTimeAtMidnight().toString("E", locale) + ")";
    }
    
    public String getPresentationEndDate() {
	return getPeriod().getEndYearMonthDay().toString("dd MMMM yyyy", locale) + " (" + getPeriod().getEndYearMonthDay().toDateTimeAtMidnight().toString("E", locale) + ")";
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
    
    public static boolean periodQuinzenalContainsWeekPeriod(Calendar startDate, Calendar endDate,
            int startWeek, DiaSemana weekDay, Calendar day, Calendar endDay, OccupationPeriod nextPeriod) {
      
	List<Calendar> listWeekly = weeklyDatesInPeriod(day, endDay, weekDay, nextPeriod);
        List<Calendar> listQuinzenal = quinzenalDatesInPeriod(startDate, endDate, startWeek, weekDay);
        for (int i = 0; i < listQuinzenal.size(); i++) {
            Calendar quinzenalDate = (Calendar) listQuinzenal.get(i);
            for (int j = 0; j < listWeekly.size(); j++) {
                Calendar date = (Calendar) listWeekly.get(j);
                if (CalendarUtil.equalDates(quinzenalDate, date)) {
                    return true;
                }
                if (date.after(quinzenalDate)) {
                    break;
                }
            }
        }
        return false;
    }    
    
    public static List<Calendar> quinzenalDatesInPeriod(Calendar startDate, Calendar endDate, int startWeek, DiaSemana weekDay) {
	       
	List<Calendar> list = new ArrayList<Calendar>();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(startDate.getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        if (startWeek == 2) {
            date.add(Calendar.DATE, 7);
        }

        if (!date.after(endDate)) {
            list.add(date);
        }
        return list;
    }

    public static List<Calendar> weeklyDatesInPeriod(Calendar day, Calendar endDay, DiaSemana weekDay, OccupationPeriod nextPeriod) {
	
        ArrayList<Calendar> list = new ArrayList<Calendar>();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(day.getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        while (true) {
            if (date.after(endDay)) {
                if (nextPeriod == null) {
                    break;
                }
                int interval = nextPeriod.getStartDate().get(Calendar.DAY_OF_YEAR)
                        - endDay.get(Calendar.DAY_OF_YEAR) - 1;
                if (interval < 0) {
                    interval = nextPeriod.getStartDate().get(Calendar.DAY_OF_YEAR) - 1;
                    interval += (endDay.getActualMaximum(Calendar.DAY_OF_YEAR) - endDay
                            .get(Calendar.DAY_OF_YEAR));
                }

                day = nextPeriod.getStartDate();
                endDay = nextPeriod.getEndDate();
                nextPeriod = nextPeriod.getNextPeriod();

                int weeksToJump = interval / 7;
                date.add(Calendar.DATE, 7 * weeksToJump);
                if (date.before(day)) {
                    date.add(Calendar.DATE, 7);
                }
            } else {
                Calendar dateToAdd = Calendar.getInstance();
                dateToAdd.setTimeInMillis(date.getTimeInMillis());
                list.add(dateToAdd);
                date.add(Calendar.DATE, 7);
            }

        }
        return list;
    }    
}
