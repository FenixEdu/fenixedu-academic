package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;

public class Schedule extends Schedule_Base {
    
	public Schedule() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	// Return true if the Schedule is valid in the interval
	public boolean isDefinedInInterval(DateInterval interval) {
		return getValidInterval().containsInterval(interval);
	}

	// Return true if the Schedule valid interval constains date
	public boolean isDefinedInDate(YearMonthDay date) {
		return getValidInterval().containsDate(date);
	}

	// Returns the Employee's work schedule for a particular date
    public WorkSchedule workScheduleWithDate(YearMonthDay date) {
    		Iterator<WorkSchedule> workSchedulesIt = getWorkScheduleIterator();
    		while(workSchedulesIt.hasNext()) {
//    			System.out.println("iterando nos wk");
    			WorkSchedule workSchedule = workSchedulesIt.next();
    			if (workSchedule.isDefinedInDate(date)) {
    				return workSchedule;
    			}
    		}
    		return null;
    }
    
        
}
