package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Duration;
import org.joda.time.DateTime;

/**
 *
 * @author velouria@velouria.org
 * 
 */
public class WorkSchedule extends WorkSchedule_Base {
    
    public WorkSchedule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    
    // Returns the duration of normal period times the number of days per week the Employee has that schedule
	public Duration calculateWeekDuration() {
		return Duration.ZERO;
		//	    return new Duration(getWorkScheduleType().getNormalWorkPeriod().getTotalNormalWorkPeriodDuration().getMillis() * getWorkWeek().workDaysPerWeek());
	}
    
    // Returns true if the schedule is defined in the week day weekDay
    public boolean isDefinedInWeekDay(WeekDay weekDay) {
    		return false;
    	// return getWorkWeek().worksAt(weekDay);
    }

    
    
    

    
    
//	// validates and creates the Valid From To Interval
//    // TODO find a decent name for this... god it sucks!
//    public static Interval createValidFromToInterval(Integer startYear, Integer startMonth, Integer startDay, Integer endYear, Integer endMonth, Integer endDay) {
//        // 	data validade - usada como data nos TimeIntervals do horario
//        DateTime startScheduleDate = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), 0, 0, 0, 0);
//        // end day might not be defined
//        DateTime endScheduleDate = null;
//        if ((endYear != null) && (endMonth != null) && (endDay != null)) {
//            System.out.println("dafa final def");
//            endScheduleDate = new DateTime(endYear.intValue(), endMonth.intValue(), endDay.intValue(), 0, 0, 0, 0);
//        } else {
//            System.out.println("dafa final nao def");
//            endScheduleDate = DomainConstants.FAR_FUTURE;
//        }
//        return new Interval(startScheduleDate, endScheduleDate);
//    }


}
