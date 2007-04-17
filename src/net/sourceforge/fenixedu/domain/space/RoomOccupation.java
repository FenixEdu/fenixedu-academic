/*
 * Created on 9/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

/**
 * @author Ana e Ricardo
 * 
 */
public class RoomOccupation extends RoomOccupation_Base {
   
    public static final Comparator<RoomOccupation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
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
    
    
    public RoomOccupation() {
    	super();    	
        setRootDomainObject(RootDomainObject.getInstance());       
    }
            
    public RoomOccupation(OldRoom room, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek, 
	    OccupationPeriod period, WrittenEvaluation writtenEvaluation) {	
        
	this();
        setRoom(room);
        setStartTime(startTime);
        setEndTime(endTime);
        setDayOfWeek(dayOfWeek);
        setWrittenEvaluation(writtenEvaluation);
        setPeriod(period);
    }    
    
    public RoomOccupation(OldRoom room, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek, Integer frequency, OccupationPeriod occupationPeriod) {	
        
	this();
        setRoom(room);
        setStartTime(startTime);
        setEndTime(endTime);
        setDayOfWeek(dayOfWeek);
        setFrequency(frequency);
        setPeriod(occupationPeriod);
    }   
    
    public RoomOccupation(OldRoom room, YearMonthDay beginDate, YearMonthDay endDate,
	    HourMinuteSecond beginTime, HourMinuteSecond endTime, FrequencyType frequencyType, GenericEvent genericEvent,
	    Boolean markSaturday, Boolean markSunday) {	
	
        this();          
        
        DiaSemana diaSemana = new DiaSemana(DiaSemana.getDiaSemana(beginDate));
        Integer frequency = (frequencyType != null) ? frequencyType.ordinal() + 1 : null;        
        Calendar beginTimeCalendar = beginDate.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0, 0)).toCalendar(LanguageUtils.getLocale());
        Calendar endTimeCalendar = endDate.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0, 0)).toCalendar(LanguageUtils.getLocale());
        
        OccupationPeriod occupationPeriod = OccupationPeriod.readOccupationPeriod(beginDate, endDate);
        if(occupationPeriod == null) {
            occupationPeriod = new OccupationPeriod(beginDate, endDate);
        }
                       
        if(frequencyType != null &&
        	frequencyType.equals(FrequencyType.DAILY) && (markSaturday == null || markSunday == null)) {
            throw new DomainException("error.roomOccupation.invalid.dailyFrequency");
        }
        
        if(!room.isFree(occupationPeriod, beginTimeCalendar, endTimeCalendar, diaSemana, frequency, 1, markSaturday, markSunday)) {
            throw new DomainException("error.roomOccupation.room.is.not.free");
        }
        
        setDailyFrequencyMarkSaturday(markSaturday);
        setDailyFrequencyMarkSunday(markSunday);
        setRoom(room);
        setGenericEvent(genericEvent);
        setStartTime(beginTimeCalendar);
        setEndTime(endTimeCalendar);
        setDayOfWeek(diaSemana);                       
        setPeriod(occupationPeriod);
        setFrequency(frequencyType);               
    }   

    public boolean roomOccupationForDateAndTime(OccupationPeriod period, Calendar startTime,
	    Calendar endTime, DiaSemana dayOfWeek, Integer frequency, Integer week, OldRoom room,
	    Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {

	if (!room.equals(this.getRoom())) {
	    return false;
	}

	return roomOccupationForDateAndTime(period.getStartDate(), period.getEndDate(), startTime,
		endTime, dayOfWeek, frequency, week, dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday);
    }

    public boolean roomOccupationForDateAndTime(Calendar startDate, Calendar endDate, Calendar startTime, Calendar endTime, 
	    DiaSemana dayOfWeek, Integer frequency, Integer week, Boolean dailyFrequencyMarkSaturday,
	    Boolean dailyFrequencyMarkSunday) {
      
	startTime.set(Calendar.SECOND, 0);
	startTime.set(Calendar.MILLISECOND, 0);
	endTime.set(Calendar.SECOND, 0);
	endTime.set(Calendar.MILLISECOND, 0);
	
	if (getPeriod().nestedOccupationPeriodsIntersectDates(startDate, endDate)) {                       	   	    	    			   
	    
	    List<Interval> thisOccupationIntervals = getRoomOccupationIntervals();
	    List<Interval> passedOccupationIntervals = getRoomOccupationIntervals(new YearMonthDay(startDate), new YearMonthDay(endDate),  
		    new HourMinuteSecond(startTime.getTime()), new HourMinuteSecond(endTime.getTime()), frequency, week, dayOfWeek,
		    dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday);		
	    			    	  
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

    public Integer getFrequency() {
        if (getLesson() != null) {
            return getLesson().getFrequency();
        }        
        if (getGenericEvent() != null && getGenericEvent().getFrequency() != null) {                       
            return getGenericEvent().getFrequency().ordinal() + 1;
        }
        return null;
    }
    
    public void setFrequency(Integer frequency) {
        if(getLesson() != null) {
            getLesson().setFrequency(frequency); 
        }      
    }
    
    public void setFrequency(FrequencyType type) {
	if (getGenericEvent() != null) {
	    getGenericEvent().setFrequency(type);
	}
    }
    
    public Integer getWeekOfQuinzenalStart() {
        return getLesson() != null ? getLesson().getWeekOfQuinzenalStart() : null;
    }
    
    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
        if(getLesson() != null) {
            getLesson().setWeekOfQuinzenalStart(weekOfQuinzenalStart); 
        }        
    }              
   
    public void delete() {
        final OccupationPeriod period = getPeriod();

        removeLesson();
        removeWrittenEvaluation();
        removeGenericEvent();
        removeRoom();
        removePeriod();

        if (period != null) {
            period.delete();
        }

        removeRootDomainObject();
        super.deleteDomainObject();
    }
       
    public DateTime getFirstInstant() {		
	List<Interval> roomOccupationIntervals = getRoomOccupationIntervals();
	return (roomOccupationIntervals != null && !roomOccupationIntervals.isEmpty()) ?
		roomOccupationIntervals.get(0).getStart() : null;	
    }
    
    public DateTime getLastInstant() {	
	List<Interval> roomOccupationIntervals = getRoomOccupationIntervals();
	return (roomOccupationIntervals != null && !roomOccupationIntervals.isEmpty()) ?
		roomOccupationIntervals.get(roomOccupationIntervals.size() - 1).getEnd() : null;	
    }                    
    
    public List<Interval> getRoomOccupationIntervals(YearMonthDay begin, YearMonthDay end){
	List<Interval> result = new ArrayList<Interval>();
	for (Interval interval : getRoomOccupationIntervals()) {
	    if(interval.getStart().toYearMonthDay().isAfter(end)) {
		break;	    
	    } else if(!interval.getStart().toYearMonthDay().isAfter(end) && 
		    !interval.getEnd().toYearMonthDay().isBefore(begin)) {
		result.add(interval);
	    }
	}
	return result;
    }
    
    public List<Interval> getRoomOccupationIntervals() {		
	List<Interval> result = new ArrayList<Interval>();
	OccupationPeriod occupationPeriod = getPeriod();        
	
	result.addAll(getRoomOccupationIntervals(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay(), 
    	    getStartTimeDateHourMinuteSecond(), getEndTimeDateHourMinuteSecond(), getFrequency(), getWeekOfQuinzenalStart(), 
    	    getDayOfWeek(), getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday()));
	                             		           
        while(occupationPeriod.getNextPeriod() != null) {
            result.addAll(getRoomOccupationIntervals(occupationPeriod.getNextPeriod().getStartYearMonthDay(), 
        	    occupationPeriod.getNextPeriod().getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(),
        	    getEndTimeDateHourMinuteSecond(), getFrequency(), getWeekOfQuinzenalStart(), getDayOfWeek(),
        	    getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday()));
            
            occupationPeriod = occupationPeriod.getNextPeriod();             
        }
                
	return result;            		
    }	
          
    private List<Interval> getRoomOccupationIntervals(YearMonthDay begin, YearMonthDay end,
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
    
    @Override
    public void setStartTimeDateHourMinuteSecond(HourMinuteSecond startTimeDateHourMinuteSecond) {
        final HourMinuteSecond hourMinuteSecond = eliminateSeconds(startTimeDateHourMinuteSecond);
        super.setStartTimeDateHourMinuteSecond(hourMinuteSecond);
    }

    @Override
    public void setEndTimeDateHourMinuteSecond(HourMinuteSecond endTimeDateHourMinuteSecond) {
        final HourMinuteSecond hourMinuteSecond = eliminateSeconds(endTimeDateHourMinuteSecond);
        super.setEndTimeDateHourMinuteSecond(hourMinuteSecond);
    }

    private HourMinuteSecond eliminateSeconds(final HourMinuteSecond hourMinuteSecond) {
        return hourMinuteSecond == null ? null : new HourMinuteSecond(hourMinuteSecond.getHour(), hourMinuteSecond.getMinuteOfHour(), 0);
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
       
	ArrayList<Calendar> list = new ArrayList<Calendar>();
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
	
        List<Calendar> list = new ArrayList<Calendar>();
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
    
    public Calendar getStartTime() {
        if (this.getStartTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStartTimeDate());
            return result;
        }
        return null;
    }

    public void setStartTime(Calendar calendar) {
        if (calendar != null) {
            this.setStartTimeDate(calendar.getTime());
        } else {
            this.setStartTimeDate(null);
        }
    }

    public Calendar getEndTime() {
        if (this.getEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndTimeDate());
            return result;
        }
        return null;
    }

    public void setEndTime(Calendar calendar) {
        if (calendar != null) {
            this.setEndTimeDate(calendar.getTime());
        } else {
            this.setEndTimeDate(null);
        }
    }

}
