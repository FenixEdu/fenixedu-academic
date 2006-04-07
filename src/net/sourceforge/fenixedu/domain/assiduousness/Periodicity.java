package net.sourceforge.fenixedu.domain.assiduousness;

import org.joda.time.YearMonthDay;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Periodicity extends Periodicity_Base {
    
    public  Periodicity() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    
    public static Periodicity createPeriodicity(Interval definedInterval, WorkWeek workWeek) {
    		Periodicity periodicity = new Periodicity();
    		periodicity.setDefinedInterval(definedInterval);
    		periodicity.setWorkWeek(workWeek);
    		return periodicity;
    }
    
    // Return true if definedInterval contains the date and the date's day of week is in WorkWeek
    public boolean isDefinedInDate(YearMonthDay date) {
    		DateTime dateAtMidnight = date.toDateTimeAtMidnight();
    		if (getDefinedInterval().contains(dateAtMidnight) && getWorkWeek().contains(dateAtMidnight)) {
    			return true;
    		}
    		return false;
    }
    
}
