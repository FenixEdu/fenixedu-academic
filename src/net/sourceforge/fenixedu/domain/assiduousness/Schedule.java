package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateUtilities;
import net.sourceforge.fenixedu.util.WeekDay;

public class Schedule extends Schedule_Base {
    
    public Schedule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
 
    public WorkSchedule workScheduleWithDate(YearMonthDay date) {
        DateTime dateAtMidnight = date.toDateTimeAtMidnight();
        WeekDay weekday = DateUtilities.convertToWeekDays(dateAtMidnight.getDayOfWeek());
        Iterator<WorkSchedule> workSchedulesIt = getWorkScheduleIterator();
        while(workSchedulesIt.hasNext()) {
            WorkSchedule workSchedule = workSchedulesIt.next();
            if (workSchedule.getValidInterval().contains(dateAtMidnight) && workSchedule.isDefinedInWeekDay(weekday)) {
                return workSchedule;
            }
        }
        return null;
    }
    
    
    
}
