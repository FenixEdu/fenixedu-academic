package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.util.WeekDay;

public class LeaveBean implements Serializable {

    private DomainReference<Leave> leave;

    private DateTime date;
    
    private YearMonthDay endYearMonthDay;
    
    public LeaveBean(Leave leave) {
	setLeave(leave);
	setDate(leave.getDate());
	setEndYearMonthDay(leave.getEndYearMonthDay());
    }
    
    public LeaveBean(Leave leave, Leave nextLeave) {
	setLeave(leave);
	setDate(leave.getDate());
	setEndYearMonthDay(nextLeave.getEndYearMonthDay());
    }
//    
//    public int getUtilDaysBetween(Interval interval) {
//        int days = 0;
//        for (YearMonthDay thisDay = interval.getStart().toYearMonthDay(); !thisDay.isAfter(interval
//                .getEnd().toYearMonthDay()); thisDay = thisDay.plusDays(1)) {
//            WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(thisDay.toDateTimeAtMidnight());
//            if ((!dayOfWeek.equals(WeekDay.SATURDAY)) && (!dayOfWeek.equals(WeekDay.SUNDAY))
//                    && (!getLeave().getAssiduousness().isHoliday(thisDay))) {
//                days++;
//            }
//        }
//        return days;
//    }
    
    public Leave getLeave() {
	return leave != null ? leave.getObject() : null;
    }

    public void setLeave(Leave leave) {
	this.leave = leave != null ? new DomainReference<Leave>(leave) : null;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public YearMonthDay getEndYearMonthDay() {
        return endYearMonthDay;
    }

    public void setEndYearMonthDay(YearMonthDay endYearMonthDay) {
        this.endYearMonthDay = endYearMonthDay;
    }
}
