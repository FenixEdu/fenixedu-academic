/*
 * Created on 14/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 * 
 */
public class OccupationPeriod extends OccupationPeriod_Base {

    private OccupationPeriod() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public OccupationPeriod(Date startDate, Date endDate) {
    	this();
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        this.setStart(startDate);
        this.setEnd(endDate);
    }
    
    public OccupationPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        this();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
        setStartYearMonthDay(startDate);
        setEndYearMonthDay(endDate);
    }   
  
    @Override
    public void setNextPeriod(OccupationPeriod nextPeriod) {
	if (!allNestedPeriodsAreEmpty()) {
	    throw new DomainException("error.occupationPeriod.previous.periods.not.empty");
	}
	if (nextPeriod != null && !nextPeriod.getStartYearMonthDay().isAfter(getEndYearMonthDay())) {
	    throw new DomainException("error.occupationPeriod.invalid.nextPeriod");
	}
	super.setNextPeriod(nextPeriod);
    }
           
    @Override
    public void setPreviousPeriod(OccupationPeriod previousPeriod) {
	if (!allNestedPeriodsAreEmpty()) {
	    throw new DomainException("error.occupationPeriod.next.periods.not.empty");
	}
	if (previousPeriod != null && !previousPeriod.getEndYearMonthDay().isBefore(getStartYearMonthDay())) {
	    throw new DomainException("error.occupationPeriod.invalid.previousPeriod");
	}
	super.setPreviousPeriod(previousPeriod);
    }
    
    public Calendar getStartDate() {
        if (this.getStart() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStart());
            return result;
        }
        return null;
    }

    public void setEndDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnd(calendar.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    public void setStartDate(Calendar calendar) {
        if (calendar != null) {
            this.setStart(calendar.getTime());
        } else {
            this.setStart(null);
        }
    }

    public Calendar getEndDateOfComposite() {
        Calendar end = this.getEndDate();
        OccupationPeriod period = this.getNextPeriod();
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }    

    private boolean intersectPeriods(final Calendar start, final Calendar end) {
        return CalendarUtil.intersectDates(start, end, getStartDate(), getEndDate());
    }
      
    private boolean intersectPeriods(YearMonthDay start, YearMonthDay end) {
        return !getStartYearMonthDay().isAfter(end) && !getEndYearMonthDay().isBefore(start);
    }
    
    private boolean containsDay(YearMonthDay day) {
        return intersectPeriods(day, day);
    }
               
    public void delete() {	       
	if (allNestedPeriodsAreEmpty()) {            	    	    	               
	    removeNextPeriod();
	    removePreviousPeriod();
            removeRootDomainObject();
            deleteDomainObject();	    	    	
        }
    }   
    
    public boolean allNestedPeriodsAreEmpty() {	
	OccupationPeriod firstOccupationPeriod = getFirstOccupationPeriodOfNestedPeriods();
	if(!firstOccupationPeriod.isEmpty()) {
	    return false;
	}	
	while(firstOccupationPeriod.getNextPeriod() != null) {
	    if(!firstOccupationPeriod.getNextPeriod().isEmpty()) {
		return false;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}	
	return true;   
    }
    
    private boolean isEmpty() {
        return getRoomOccupations().isEmpty()
        	&& getExecutionDegreesForExamsSpecialSeason().isEmpty()
                && getExecutionDegreesForExamsFirstSemester().isEmpty()
                && getExecutionDegreesForExamsSecondSemester().isEmpty()
                && getExecutionDegreesForLessonsFirstSemester().isEmpty()
                && getExecutionDegreesForLessonsSecondSemester().isEmpty()
                && getExecutionDegreesForGradeSubmissionNormalSeasonFirstSemester().isEmpty()
                && getExecutionDegreesForGradeSubmissionNormalSeasonSecondSemester().isEmpty()
                && getExecutionDegreesForGradeSubmissionSpecialSeason().isEmpty();
    }
    
    public OccupationPeriod getLastOccupationPeriodOfNestedPeriods() {
	OccupationPeriod occupationPeriod = this;
	while(occupationPeriod.getNextPeriod() != null) {	    
	    occupationPeriod = occupationPeriod.getNextPeriod();
	}
	return occupationPeriod;
    }
    
    private OccupationPeriod getFirstOccupationPeriodOfNestedPeriods() {		
	OccupationPeriod occupationPeriod = this;
	while(occupationPeriod.getPreviousPeriod() != null) {	    
	    occupationPeriod = occupationPeriod.getPreviousPeriod();
	}
	return occupationPeriod;
    }
               
    public static OccupationPeriod readByDates(Date startDate, Date endDate) {
        for (OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriods()) {
            if (occupationPeriod.getNextPeriod() == null 
        	    && occupationPeriod.getPreviousPeriod() == null 
        	    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getStart(), startDate)
                    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getEnd(), endDate)) {
        	return occupationPeriod;
            }
        }
        return null;
    }

    public static OccupationPeriod readOccupationPeriod(YearMonthDay start, YearMonthDay end) {
	for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
	    if (occupationPeriod.getNextPeriod() == null 
		    && occupationPeriod.getPreviousPeriod() == null 
		    && occupationPeriod.getStartYearMonthDay().equals(start)
		    && occupationPeriod.getEndYearMonthDay().equals(end)) {
		return occupationPeriod;
	    }
	}
	return null;
    }
    
    public boolean nestedOccupationPeriodsIntersectDates(OccupationPeriod occupationPeriod) {	
	OccupationPeriod firstOccupationPeriod = this;
	while(firstOccupationPeriod != null) {
	    if(firstOccupationPeriod.intersectPeriods(occupationPeriod.getStartYearMonthDay(), 
		    occupationPeriod.getEndYearMonthDay())) {
		return true;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}	        
	return false;
    }
    
    public boolean nestedOccupationPeriodsIntersectDates(Calendar start, Calendar end) {	
	OccupationPeriod firstOccupationPeriod = this;
	while(firstOccupationPeriod != null) {
	    if(firstOccupationPeriod.intersectPeriods(start, end)) {
		return true;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}	        
	return false;
    }
    
    public boolean nestedOccupationPeriodsContainsDay(YearMonthDay day) {
	OccupationPeriod firstOccupationPeriod = this;
	while(firstOccupationPeriod != null) {
	    if(firstOccupationPeriod.containsDay(day)) {
		return true;
	    }
	    firstOccupationPeriod = firstOccupationPeriod.getNextPeriod();
	}	        
	return false;
    }
}
